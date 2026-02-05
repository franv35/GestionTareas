// ================= CONFIG =================
const API = "http://localhost:8080/api";

// ================= AUTH =================
const token = localStorage.getItem("token");
const usuarioId = localStorage.getItem("usuarioId");
const usuarioNombre = localStorage.getItem("usuarioNombre");
const usuarioEmail = localStorage.getItem("usuarioEmail");

// 🔒 Validación mínima JWT
if (!token || token.split(".").length !== 3) {
  localStorage.clear();
  window.location.href = "login.html";
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`
  };
}

// ================= USER INFO =================
const userInfo = document.getElementById("userInfo");
if (userInfo) {
  userInfo.innerText = `${usuarioNombre} · ${usuarioEmail}`;
}

// ================= ELEMENTOS =================
const proyectosGrid = document.getElementById("proyectosGrid");

// Stats
const statProyectos = document.getElementById("statProyectos");
const statProceso = document.getElementById("statProceso");
const statCompletadas = document.getElementById("statCompletadas");

// Modales
const modalProyecto = document.getElementById("modalProyecto");
const modalRecurso = document.getElementById("modalRecurso");

// Botones
const btnAbrirProyecto = document.getElementById("btnAbrirProyecto");
const btnAbrirRecurso = document.getElementById("btnAbrirRecurso");
const cerrarProyecto = document.getElementById("cerrarProyecto");
const cerrarRecurso = document.getElementById("cerrarRecurso");
const logoutBtn = document.getElementById("logoutBtn");

// Forms
const formProyecto = document.getElementById("formProyecto");
const formRecurso = document.getElementById("formRecurso");

// ================= MODALES =================
if (btnAbrirProyecto) {
  btnAbrirProyecto.onclick = () =>
    modalProyecto.classList.remove("hidden");
}

if (cerrarProyecto) {
  cerrarProyecto.onclick = () =>
    modalProyecto.classList.add("hidden");
}

if (btnAbrirRecurso) {
  btnAbrirRecurso.onclick = () =>
    modalRecurso.classList.remove("hidden");
}

if (cerrarRecurso) {
  cerrarRecurso.onclick = () =>
    modalRecurso.classList.add("hidden");
}

// Cerrar modal al clickear fuera
window.addEventListener("click", e => {
  if (e.target === modalProyecto)
    modalProyecto.classList.add("hidden");
  if (e.target === modalRecurso)
    modalRecurso.classList.add("hidden");
});

// ================= LOGOUT =================
if (logoutBtn) {
  logoutBtn.addEventListener("click", () => {
    localStorage.clear();
    window.location.href = "login.html";
  });
}

// ================= PROYECTOS =================
async function cargarProyectos() {
  try {
    const res = await fetch(
      `${API}/proyectos/usuario/${usuarioId}`,
      { headers: authHeaders() }
    );

    // 🔐 SOLO errores de autenticación
    if (res.status === 401 || res.status === 403) {
      throw new Error("AUTH_ERROR");
    }

    const proyectos = await res.json();
    console.log("Proyectos recibidos:", proyectos);

    proyectosGrid.innerHTML = "";
    statProyectos.innerText = proyectos.length;

    let enProceso = 0;
    let completadas = 0;

    // 🆕 Usuario sin proyectos (CASO VÁLIDO)
    if (proyectos.length === 0) {
      proyectosGrid.innerHTML = `
        <div class="empty-state">
          <p>No tenés proyectos todavía.</p>
          <button id="btnCrearPrimerProyecto">
            Crear mi primer proyecto
          </button>
        </div>
      `;

      document
        .getElementById("btnCrearPrimerProyecto")
        .onclick = () =>
          modalProyecto.classList.remove("hidden");

      statProceso.innerText = 0;
      statCompletadas.innerText = 0;
      return;
    }

    proyectos.forEach(p => {
      // Conteo de tareas
      if (Array.isArray(p.tareas)) {
        p.tareas.forEach(t => {
          if (t.estado === "EN_PROCESO") enProceso++;
          if (t.estado === "TERMINADA") completadas++;
        });
      }

      const card = document.createElement("div");
      card.className = "proyecto-card";

      card.innerHTML = `
        <div class="proyecto-header">
          <h3>
            <a 
              href="dashboard.html?proyectoId=${p.id}" 
              class="proyecto-link"
            >
              ${p.nombre}
            </a>
          </h3>
        </div>
        <p>${p.descripcion || "Sin descripción"}</p>
      `;

      proyectosGrid.appendChild(card);
    });

    statProceso.innerText = enProceso;
    statCompletadas.innerText = completadas;

  } catch (error) {
    console.error("Error cargando proyectos:", error);
    localStorage.clear();
    window.location.href = "login.html";
  }
}

// ================= CREAR PROYECTO =================
if (formProyecto) {
  formProyecto.onsubmit = async e => {
    e.preventDefault();

    const body = {
      nombre: document.getElementById("proyectoNombre").value.trim(),
      descripcion: document.getElementById("proyectoDescripcion").value.trim(),
      fechaInicio: document.getElementById("proyectoFecha").value
    };

    if (!body.nombre) {
      alert("El nombre del proyecto es obligatorio");
      return;
    }

    const res = await fetch(
      `${API}/proyectos/usuario/${usuarioId}`,
      {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify(body)
      }
    );

    if (!res.ok) {
      alert("Error al crear proyecto");
      return;
    }

    modalProyecto.classList.add("hidden");
    formProyecto.reset();
    cargarProyectos();
  };
}

// ================= CREAR RECURSO =================
if (formRecurso) {
  formRecurso.onsubmit = async e => {
    e.preventDefault();

    const body = {
      nombre: document.getElementById("recursoNombre").value.trim(),
      cantidad: document.getElementById("recursoCantidad").value,
      unidadMedida: document.getElementById("recursoUnidad").value.trim()
    };

    if (!body.nombre || !body.cantidad || !body.unidadMedida) {
      alert("Completá todos los campos del recurso");
      return;
    }

    const res = await fetch(`${API}/recursos`, {
      method: "POST",
      headers: authHeaders(),
      body: JSON.stringify(body)
    });

    if (!res.ok) {
      alert("Error al crear recurso");
      return;
    }

    modalRecurso.classList.add("hidden");
    formRecurso.reset();
  };
}

// ================= INIT =================
cargarProyectos();
