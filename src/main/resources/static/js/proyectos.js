// ================= CONFIG =================
const API = "http://localhost:8080/api";

// ================= AUTH =================
const token = localStorage.getItem("token");
const usuarioId = localStorage.getItem("usuarioId");
const usuarioNombre = localStorage.getItem("usuarioNombre");
const usuarioEmail = localStorage.getItem("usuarioEmail");

// 🔒 Validación mínima de JWT
if (!token || token.split(".").length !== 3) {
  console.warn("Token inválido o inexistente");
  localStorage.clear();
  window.location.href = "login.html";
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
  btnAbrirProyecto.onclick = () => modalProyecto.classList.remove("hidden");
}

if (cerrarProyecto) {
  cerrarProyecto.onclick = () => modalProyecto.classList.add("hidden");
}

if (btnAbrirRecurso) {
  btnAbrirRecurso.onclick = () => modalRecurso.classList.remove("hidden");
}

if (cerrarRecurso) {
  cerrarRecurso.onclick = () => modalRecurso.classList.add("hidden");
}

// Cerrar modal al hacer click fuera
window.addEventListener("click", e => {
  if (e.target === modalProyecto) modalProyecto.classList.add("hidden");
  if (e.target === modalRecurso) modalRecurso.classList.add("hidden");
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
    const res = await fetch(`${API}/usuarios/${usuarioId}/proyectos`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (res.status === 401 || res.status === 403) {
      throw new Error("TOKEN_INVALIDO");
    }

    const proyectos = await res.json();

    proyectosGrid.innerHTML = "";
    statProyectos.innerText = proyectos.length;

    let enProceso = 0;
    let completadas = 0;

    proyectos.forEach(p => {
      if (p.tareas) {
        p.tareas.forEach(t => {
          if (t.estado === "EN_PROCESO") enProceso++;
          if (t.estado === "TERMINADA") completadas++;
        });
      }

      const card = document.createElement("div");
      card.className = "proyecto-card";
      card.innerHTML = `
        <h4>${p.nombre}</h4>
        <p>${p.descripcion || "Sin descripción"}</p>
      `;
      proyectosGrid.appendChild(card);
    });

    statProceso.innerText = enProceso;
    statCompletadas.innerText = completadas;

  } catch (error) {
    console.error("Error cargando proyectos:", error);

    // 🔐 Token vencido o inválido
    localStorage.clear();
    window.location.href = "login.html";
  }
}

// ================= CREAR PROYECTO =================
if (formProyecto) {
  formProyecto.onsubmit = async e => {
    e.preventDefault();

    const body = {
      nombre: document.getElementById("proyectoNombre").value,
      descripcion: document.getElementById("proyectoDescripcion").value,
      fechaInicio: document.getElementById("proyectoFecha").value
    };

    const res = await fetch(`${API}/proyectos/usuario/${usuarioId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(body)
    });

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
      nombre: document.getElementById("recursoNombre").value,
      cantidad: document.getElementById("recursoCantidad").value,
      unidad: document.getElementById("recursoUnidad").value
    };

    const res = await fetch(`${API}/recursos`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`
      },
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
