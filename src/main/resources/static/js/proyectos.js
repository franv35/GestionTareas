/* ======================================================
   CONFIG
====================================================== */
const API = "http://localhost:8080/api";

/* ======================================================
   AUTH
====================================================== */
const token = localStorage.getItem("token");
const usuarioId = localStorage.getItem("usuarioId");
const usuarioNombre = localStorage.getItem("usuarioNombre");
const usuarioEmail = localStorage.getItem("usuarioEmail");

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

/* ======================================================
   USER INFO
====================================================== */
const userInfo = document.getElementById("userInfo");
if (userInfo) {
  userInfo.innerText = `${usuarioNombre} · ${usuarioEmail}`;
}

/* ======================================================
   ELEMENTOS
====================================================== */
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

/* ======================================================
   MODALES
====================================================== */
btnAbrirProyecto?.addEventListener("click", () => {
  modalProyecto.classList.remove("hidden");
});

btnAbrirRecurso?.addEventListener("click", () => {
  modalRecurso.classList.remove("hidden");
});

cerrarProyecto?.addEventListener("click", () => {
  modalProyecto.classList.add("hidden");
});

cerrarRecurso?.addEventListener("click", () => {
  modalRecurso.classList.add("hidden");
});

// click fuera
window.addEventListener("click", e => {
  if (e.target === modalProyecto) modalProyecto.classList.add("hidden");
  if (e.target === modalRecurso) modalRecurso.classList.add("hidden");
});

/* ======================================================
   LOGOUT
====================================================== */
logoutBtn?.addEventListener("click", () => {
  localStorage.clear();
  window.location.href = "login.html";
});

/* ======================================================
   CARGAR PROYECTOS
====================================================== */
async function cargarProyectos() {
  try {
    const res = await fetch(
      `${API}/proyectos/usuario/${usuarioId}`,
      { headers: authHeaders() }
    );

    if (!res.ok) throw new Error("Error auth");

    let proyectos = await res.json();

    if (!Array.isArray(proyectos)) {
      proyectos = [];
    }

    proyectosGrid.innerHTML = "";
    statProyectos.innerText = proyectos.length;

    let enProceso = 0;
    let completadas = 0;

    // 🔹 SIN proyectos
    if (proyectos.length === 0) {
      proyectosGrid.innerHTML = `
        <div class="empty-state">
          <p>No tenés proyectos todavía.</p>
          <button class="btn" id="btnCrearPrimerProyecto">
            Crear mi primer proyecto
          </button>
        </div>
      `;

      document
        .getElementById("btnCrearPrimerProyecto")
        .addEventListener("click", () => {
          modalProyecto.classList.remove("hidden");
        });

      statProceso.innerText = 0;
      statCompletadas.innerText = 0;
      return;
    }

    // 🔹 CON proyectos
    proyectos.forEach(p => {

      if (Array.isArray(p.tareas)) {
        p.tareas.forEach(t => {
          if (t.estado === "EN_PROCESO") enProceso++;
          if (t.estado === "TERMINADA") completadas++;
        });
      }

      const card = document.createElement("div");
      card.className = "proyecto-card";

      card.innerHTML = `
        <h3>${p.nombre}</h3>
        <p>${p.descripcion || "Sin descripción"}</p>

        ${p.estado === "TERMINADO" 
          ? "<p style='color:#2e7d32;font-weight:bold;'>Proyecto terminado</p>" 
          : ""}

        <div class="proyecto-actions">
          <button class="btn-editar" data-id="${p.id}">✏ Editar</button>
          <button class="btn-eliminar" data-id="${p.id}">🗑 Eliminar</button>
          ${p.estado !== "TERMINADO"
            ? `<button class="btn-terminar" data-id="${p.id}">✅ Terminar</button>`
            : ""}
        </div>
      `;

      proyectosGrid.appendChild(card);
    });

    statProceso.innerText = enProceso;
    statCompletadas.innerText = completadas;

    // 🔥 ELIMINAR
    document.querySelectorAll(".btn-eliminar").forEach(btn => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.id;

        if (!confirm("¿Eliminar proyecto?")) return;

        await fetch(`${API}/proyectos/${id}`, {
          method: "DELETE",
          headers: authHeaders()
        });

        cargarProyectos();
      });
    });

    // 🔥 EDITAR
    document.querySelectorAll(".btn-editar").forEach(btn => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.id;

        const res = await fetch(`${API}/proyectos/${id}`, {
          headers: authHeaders()
        });

        const proyecto = await res.json();

        proyectoNombre.value = proyecto.nombre;
        proyectoDescripcion.value = proyecto.descripcion || "";

        formProyecto.onsubmit = async (e) => {
          e.preventDefault();

          await fetch(`${API}/proyectos/${id}`, {
            method: "PUT",
            headers: authHeaders(),
            body: JSON.stringify({
              nombre: proyectoNombre.value,
              descripcion: proyectoDescripcion.value
            })
          });

          modalProyecto.classList.add("hidden");
          cargarProyectos();
        };

        modalProyecto.classList.remove("hidden");
      });
    });

    // 🔥 TERMINAR
    document.querySelectorAll(".btn-terminar").forEach(btn => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.id;

        await fetch(`${API}/proyectos/${id}/terminar`, {
          method: "PUT",
          headers: authHeaders()
        });

        cargarProyectos();
      });
    });

  } catch (err) {
    console.error(err);
    localStorage.clear();
    window.location.href = "login.html";
  }
}

/* ======================================================
   CREAR PROYECTO
====================================================== */
formProyecto?.addEventListener("submit", async e => {
  e.preventDefault();

  const body = {
    nombre: proyectoNombre.value.trim(),
    descripcion: proyectoDescripcion.value.trim(),
    fechaInicio: proyectoFecha.value
  };

  if (!body.nombre) {
    alert("El nombre es obligatorio");
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
});

/* ======================================================
   CREAR RECURSO (GLOBAL)
====================================================== */
formRecurso?.addEventListener("submit", async e => {
  e.preventDefault();

  const body = {
    nombre: recursoNombre.value.trim(),
    cantidad: parseInt(recursoCantidad.value),
    unidadMedida: recursoUnidad.value.trim()
  };

  if (!body.nombre || !body.cantidad || !body.unidadMedida) {
    alert("Completá todos los campos");
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
});

/* ======================================================
   INIT
====================================================== */
cargarProyectos();
