/* ======================================================
   CONFIG
====================================================== */
const API = "http://localhost:8080/api";

/* ======================================================
   AUTH
====================================================== */
const token = localStorage.getItem("token");
const usuarioEmail = localStorage.getItem("usuarioEmail");

if (!token || token.split(".").length !== 3) {
  localStorage.clear();
  window.location.href = "login.html";
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
}

/* ======================================================
   PROYECTO CONTEXTO
====================================================== */
const params = new URLSearchParams(window.location.search);
let proyectoId =
  params.get("proyectoId") ||
  localStorage.getItem("proyectoIdActual");

if (!proyectoId) {
  alert("Proyecto no especificado");
  window.location.href = "proyectos.html";
}

localStorage.setItem("proyectoIdActual", proyectoId);

/* ======================================================
   ELEMENTOS DOM
====================================================== */
const userEmailEl = document.getElementById("userEmail");
const logoutBtn = document.getElementById("logoutBtn");

const modalTarea = document.getElementById("modalTarea");
const closeTarea = document.getElementById("closeTarea");
const formTarea = document.getElementById("formTarea");
const tareaTitulo = document.getElementById("tareaTitulo");
const tareaDescripcion = document.getElementById("tareaDescripcion");
const tareaFecha = document.getElementById("tareaFecha");
const tareaRecursos = document.getElementById("tareaRecursos");

const listaPendientes = document.getElementById("listaTareasCreadas");
const listaEnProceso = document.getElementById("listaTareasEnProceso");
const listaTerminadas = document.getElementById("listaTareasTerminadas");

const countPendientes = document.getElementById("countPendientes");
const countEnProceso = document.getElementById("countEnProceso");
const countTerminadas = document.getElementById("countTerminadas");

const modalRecursos = document.getElementById("modalRecursos");
const closeRecursos = document.getElementById("closeRecursos");
const listaRecursos = document.getElementById("listaRecursos");

const openRecursosBtn = document.getElementById("openRecursos");
const openCrearRecursoBtn = document.getElementById("openCrearRecurso");
const openCrearTareaBtn = document.getElementById("openCrearTarea");

const btnCrearRecursoModal = document.getElementById("btnCrearRecursoModal");
const recursoNombre = document.getElementById("recursoNombre");
const recursoCantidad = document.getElementById("recursoCantidad");
const recursoUnidad = document.getElementById("recursoUnidad");

const statPendientes = document.getElementById("statPendientes");
const statEnProceso = document.getElementById("statEnProceso");
const statTerminadas = document.getElementById("statTerminadas");
const statRecursos = document.getElementById("statRecursos");
const statFechaInicio = document.getElementById("statFechaInicio");

/* ======================================================
   USER INFO
====================================================== */
if (userEmailEl) userEmailEl.innerText = usuarioEmail || "—";

/* ======================================================
   LOGOUT
====================================================== */
logoutBtn?.addEventListener("click", () => {
  localStorage.clear();
  window.location.href = "login.html";
});

/* ======================================================
   MODALES
====================================================== */
function abrirModal(modal) { modal?.classList.add("open"); }
function cerrarModal(modal) { modal?.classList.remove("open"); }
function stopPropagation(e) { e.stopPropagation(); }

// Modal crear tarea
openCrearTareaBtn?.addEventListener("click", () => {
  formTarea.reset();
  tareaFecha.value = new Date().toISOString().slice(0, 10);
  abrirModal(modalTarea);
});

closeTarea?.addEventListener("click", () => cerrarModal(modalTarea));
modalTarea?.addEventListener("click", () => cerrarModal(modalTarea));
modalTarea.querySelector(".modal-content")
  ?.addEventListener("click", stopPropagation);

// Modal recursos
openRecursosBtn?.addEventListener("click", async () => {
  abrirModal(modalRecursos);
  const recursos = await fetchRecursos();
  renderListaRecursos(recursos);
});

openCrearRecursoBtn?.addEventListener("click", () => {
  abrirModal(modalRecursos);
});

closeRecursos?.addEventListener("click", () => cerrarModal(modalRecursos));
modalRecursos?.addEventListener("click", () => cerrarModal(modalRecursos));
modalRecursos.querySelector(".modal-content")
  ?.addEventListener("click", stopPropagation);

/* ======================================================
   FETCH
====================================================== */
async function fetchTareas() {
  try {
    const res = await fetch(
      `${API}/proyectos/${proyectoId}/tareas`,
      { headers: authHeaders() }
    );
    return res.ok ? await res.json() : [];
  } catch {
    return [];
  }
}

async function fetchRecursos() {
  try {
    const res = await fetch(
      `${API}/recursos?proyectoId=${proyectoId}`,
      { headers: authHeaders() }
    );
    return res.ok ? await res.json() : [];
  } catch {
    return [];
  }
}

/* ======================================================
   RENDER TAREAS
====================================================== */
function renderTareas(listaEl, tareas) {
  listaEl.innerHTML = "";

  if (!tareas.length) {
    const li = document.createElement("li");
    li.className = "empty-placeholder";
    li.textContent = "No hay tareas en esta sección";
    listaEl.appendChild(li);
    return;
  }

  tareas.forEach(t => {
    const li = document.createElement("li");
    li.className = "tarea-card";

    li.innerHTML = `
      <strong>${t.titulo}</strong>
      <span>${t.descripcion || "-"}</span>
      <small>${t.fecha || ""}</small>
      <em>Recursos: ${
        t.recursos?.length
          ? t.recursos.map(r => r.nombre).join(", ")
          : "—"
      }</em>
      <div class="actions">
        ${
          t.estado === "PENDIENTE"
            ? `<button class="btn-move" data-id="${t.id}" data-estado="EN_PROGRESO">Iniciar</button>`
            : ""
        }
        ${
          t.estado === "EN_PROGRESO"
            ? `<button class="btn-complete" data-id="${t.id}" data-estado="COMPLETADA">Finalizar</button>`
            : ""
        }
        ${
          t.estado === "EN_PROGRESO"
            ? `<button class="btn-move" data-id="${t.id}" data-estado="PENDIENTE">Volver</button>`
            : ""
        }
        <button class="btn-delete" data-id="${t.id}">Eliminar</button>
      </div>
    `;

    listaEl.appendChild(li);
  });

  attachDeleteHandlers();
  attachMoveHandlers();
}

/* ======================================================
   RENDER RECURSOS
====================================================== */
function renderRecursos(recursos) {
  tareaRecursos.innerHTML = "";

  if (!recursos.length) {
    const opt = document.createElement("option");
    opt.textContent = "No hay recursos creados";
    opt.disabled = true;
    opt.selected = true;
    tareaRecursos.appendChild(opt);
    return;
  }

  recursos.forEach(r => {
    const opt = document.createElement("option");
    opt.value = r.id;
    opt.textContent =
      `${r.nombre} (${r.stockDisponible}/${r.stockTotal} ${r.unidad})`;
    tareaRecursos.appendChild(opt);
  });
}

function renderListaRecursos(recursos) {
  listaRecursos.innerHTML = "";

  if (!recursos.length) {
    listaRecursos.innerHTML = "<li>No hay recursos aún</li>";
    return;
  }

  recursos.forEach(r => {
    const li = document.createElement("li");
    li.textContent =
      `${r.nombre} - ${r.stockDisponible}/${r.stockTotal} ${r.unidad}`;
    listaRecursos.appendChild(li);
  });
}

/* ======================================================
   HANDLERS DINÁMICOS
====================================================== */
function attachDeleteHandlers() {
  document.querySelectorAll(".btn-delete").forEach(btn => {
    btn.onclick = async () => {
      if (!confirm("¿Eliminar tarea?")) return;
      await fetch(`${API}/tareas/${btn.dataset.id}`, {
        method: "DELETE",
        headers: authHeaders(),
      });
      refreshAll();
    };
  });
}

function attachMoveHandlers() {
  document.querySelectorAll(".btn-move, .btn-complete")
    .forEach(btn => {
      btn.onclick = async () => {
        await fetch(`${API}/tareas/${btn.dataset.id}/estado`, {
          method: "PUT",
          headers: authHeaders(),
          body: JSON.stringify({
            estado: btn.dataset.estado
          }),
        });
        refreshAll();
      };
    });
}

/* ======================================================
   CREATE TAREA
====================================================== */
formTarea?.addEventListener("submit", async e => {
  e.preventDefault();

  const titulo = tareaTitulo.value.trim();
  const descripcion = tareaDescripcion.value.trim();
  const fecha = tareaFecha.value;

  if (!titulo || !fecha) {
    alert("Completá título y fecha");
    return;
  }

  const recursosSeleccionados =
    Array.from(tareaRecursos.selectedOptions)
      .map(o => o.value);

  const res = await fetch(
    `${API}/tareas/proyecto/${proyectoId}`,
    {
      method: "POST",
      headers: authHeaders(),
      body: JSON.stringify({
        titulo,
        descripcion,
        fecha,
        recursos: recursosSeleccionados,
      }),
    }
  );

  if (!res.ok) {
    alert("Error al crear tarea");
    return;
  }

  formTarea.reset();
  tareaFecha.value =
    new Date().toISOString().slice(0, 10);

  cerrarModal(modalTarea);
  refreshAll();
});

/* ======================================================
   CREATE RECURSO
====================================================== */
btnCrearRecursoModal?.addEventListener("click", async () => {

  const nombre = recursoNombre.value.trim();
  const cantidad = parseInt(recursoCantidad.value, 10);
  const unidad = recursoUnidad.value.trim();

  if (!nombre || !cantidad || !unidad) {
    alert("Completá todos los campos");
    return;
  }

  const res = await fetch(`${API}/recursos`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({
      nombre,
      unidad,
      stockTotal: cantidad,
      stockDisponible: cantidad,
      proyectoId,
    }),
  });

  if (!res.ok) {
    alert("Error al crear recurso");
    return;
  }

  recursoNombre.value = "";
  recursoCantidad.value = "";
  recursoUnidad.value = "";

  const recursos = await fetchRecursos();
  renderRecursos(recursos);
  renderListaRecursos(recursos);

  refreshAll();
});

/* ======================================================
   REFRESH
====================================================== */
async function refreshAll() {

  const [tareas, recursos] =
    await Promise.all([
      fetchTareas(),
      fetchRecursos()
    ]);

  const pendientes =
    tareas.filter(t => t.estado === "PENDIENTE");

  const enProceso =
    tareas.filter(t => t.estado === "EN_PROGRESO");

  const terminadas =
    tareas.filter(t => t.estado === "COMPLETADA");

  countPendientes.textContent = pendientes.length;
  countEnProceso.textContent = enProceso.length;
  countTerminadas.textContent = terminadas.length;

  statPendientes.textContent = pendientes.length;
  statEnProceso.textContent = enProceso.length;
  statTerminadas.textContent = terminadas.length;
  statRecursos.textContent = recursos.length;

  if (tareas.length &&
      tareas[0].proyecto?.fechaInicio) {
    statFechaInicio.textContent =
      tareas[0].proyecto.fechaInicio;
  }

  renderTareas(listaPendientes, pendientes);
  renderTareas(listaEnProceso, enProceso);
  renderTareas(listaTerminadas, terminadas);
  renderRecursos(recursos);
  renderListaRecursos(recursos);
}

/* ======================================================
   INIT
====================================================== */
(() => {
  tareaFecha.value =
    new Date().toISOString().slice(0, 10);
  refreshAll();
})();