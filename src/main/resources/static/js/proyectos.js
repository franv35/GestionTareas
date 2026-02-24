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
   ELEMENTOS
====================================================== */
const userInfo = document.getElementById("userInfo");
userInfo && (userInfo.innerText = `${usuarioNombre} · ${usuarioEmail}`);

const proyectosGrid = document.getElementById("proyectosGrid");
const statProyectos = document.getElementById("statProyectos");
const statProceso = document.getElementById("statProceso");
const statCompletadas = document.getElementById("statCompletadas");

const modalCrearProyecto = document.getElementById("modalCrearProyecto");
const modalEditarProyecto = document.getElementById("modalEditarProyecto");
const modalRecurso = document.getElementById("modalRecurso");

const btnAbrirProyecto = document.getElementById("btnAbrirProyecto");
const btnAbrirRecurso = document.getElementById("btnAbrirRecurso");
const cerrarCrearProyecto = document.getElementById("cerrarCrearProyecto");
const cerrarEditarProyecto = document.getElementById("cerrarEditarProyecto");
const cerrarRecurso = document.getElementById("cerrarRecurso");
const logoutBtn = document.getElementById("logoutBtn");

const formCrearProyecto = document.getElementById("formCrearProyecto");
const formEditarProyecto = document.getElementById("formEditarProyecto");
const formRecurso = document.getElementById("formRecurso");

/* Inputs */
const crearProyectoNombre = document.getElementById("crearProyectoNombre");
const crearProyectoDescripcion = document.getElementById("crearProyectoDescripcion");
const crearProyectoFecha = document.getElementById("crearProyectoFecha");

const editarProyectoNombre = document.getElementById("editarProyectoNombre");
const editarProyectoDescripcion = document.getElementById("editarProyectoDescripcion");

/* ======================================================
   FUNCIONES MODALES
====================================================== */
function abrirModal(modal) {
  modal?.classList.add("open");
}
function cerrarModal(modal) {
  modal?.classList.remove("open");
}

/* Abrir/Cerrar modales */
btnAbrirProyecto?.addEventListener("click", () => abrirModal(modalCrearProyecto));
btnAbrirRecurso?.addEventListener("click", () => abrirModal(modalRecurso));

cerrarCrearProyecto?.addEventListener("click", () => cerrarModal(modalCrearProyecto));
cerrarEditarProyecto?.addEventListener("click", () => cerrarModal(modalEditarProyecto));
cerrarRecurso?.addEventListener("click", () => cerrarModal(modalRecurso));

window.addEventListener("click", e => {
  if (e.target === modalCrearProyecto) cerrarModal(modalCrearProyecto);
  if (e.target === modalEditarProyecto) cerrarModal(modalEditarProyecto);
  if (e.target === modalRecurso) cerrarModal(modalRecurso);
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
    const res = await fetch(`${API}/proyectos/usuario/${usuarioId}`, {
      headers: authHeaders()
    });

    if (!res.ok) throw new Error("Error auth");

    let proyectos = await res.json();
    if (!Array.isArray(proyectos)) proyectos = [];

    proyectosGrid.innerHTML = "";
    statProyectos.innerText = proyectos.length;

    let enProceso = 0;
    let completadas = 0;

    // ----- SIN PROYECTOS -----
    if (proyectos.length === 0) {
      proyectosGrid.innerHTML = `
        <div class="empty-state">
          <h3>📭 No tenés proyectos todavía</h3>
          <p>Creá tu primer proyecto para empezar a organizar tareas</p>
          <button class="btn" id="btnCrearPrimerProyecto">➕ Crear Proyecto</button>
        </div>
      `;
      document
        .getElementById("btnCrearPrimerProyecto")
        .addEventListener("click", () => abrirModal(modalCrearProyecto));

      statProceso.innerText = 0;
      statCompletadas.innerText = 0;
      return;
    }

    // ----- CON PROYECTOS -----
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
        <h3>
          <a href="dashboard.html?proyectoId=${p.id}" class="proyecto-link">
            ${p.nombre}
          </a>
        </h3>
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

    // ----- ELIMINAR -----
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

    // ----- EDITAR -----
    document.querySelectorAll(".btn-editar").forEach(btn => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.id;

        const res = await fetch(`${API}/proyectos/${id}`, {
          headers: authHeaders()
        });
        const proyecto = await res.json();

        editarProyectoNombre.value = proyecto.nombre;
        editarProyectoDescripcion.value = proyecto.descripcion || "";

        // Abrir modal edición
        abrirModal(modalEditarProyecto);

        // Submisión de edición
        formEditarProyecto.onsubmit = async e => {
          e.preventDefault();

          await fetch(`${API}/proyectos/${id}`, {
            method: "PUT",
            headers: authHeaders(),
            body: JSON.stringify({
              nombre: editarProyectoNombre.value,
              descripcion: editarProyectoDescripcion.value
            })
          });

          cerrarModal(modalEditarProyecto);
          cargarProyectos();
        };
      });
    });

    // ----- TERMINAR -----
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
formCrearProyecto?.addEventListener("submit", async e => {
  e.preventDefault();

  const body = {
    nombre: crearProyectoNombre.value.trim(),
    descripcion: crearProyectoDescripcion.value.trim(),
    fechaInicio: crearProyectoFecha.value
  };

  if (!body.nombre) {
    alert("El nombre es obligatorio");
    return;
  }

  const res = await fetch(`${API}/proyectos/usuario/${usuarioId}`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(body)
  });

  if (!res.ok) {
    alert("Error al crear proyecto");
    return;
  }

  cerrarModal(modalCrearProyecto);
  formCrearProyecto.reset();
  cargarProyectos();
});

/* ======================================================
   CREAR RECURSO
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

  cerrarModal(modalRecurso);
  formRecurso.reset();
});

/* ======================================================
   INIT
====================================================== */
cargarProyectos();