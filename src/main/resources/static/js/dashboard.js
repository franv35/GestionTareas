/* ================= CONFIG ================= */
const API = "http://localhost:8080/api";

/* ================= AUTH ================= */
const token = localStorage.getItem("token");
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

/* ================= PROYECTO CONTEXTO ================= */
const params = new URLSearchParams(window.location.search);
let proyectoId = params.get("proyectoId") || localStorage.getItem("proyectoIdActual");

if (!proyectoId) {
  alert("Proyecto no especificado");
  window.location.href = "proyectos.html";
}

localStorage.setItem("proyectoIdActual", proyectoId);

/* ================= USER INFO ================= */
const userInfoEl = document.getElementById("userEmail");
if (userInfoEl) {
  userInfoEl.innerText = usuarioEmail || "—";
}

/* ================= LOGOUT ================= */
document.getElementById("logoutBtn").addEventListener("click", () => {
  localStorage.clear();
  window.location.href = "login.html";
});

/* ================= MODAL TAREA ================= */
const modalTarea = document.getElementById("modalTarea");
const modalContentTarea = modalTarea.querySelector(".modal-content");
const closeTarea = document.getElementById("closeTarea");

function abrirModalTarea() {
  modalTarea.classList.add("open");
}

function cerrarModalTarea() {
  modalTarea.classList.remove("open");
}

// abrir desde sidebar
document
  .querySelector('[data-section="nueva-tarea"]')
  .addEventListener("click", abrirModalTarea);

// cerrar con X
closeTarea.addEventListener("click", e => {
  e.stopPropagation();
  cerrarModalTarea();
});

// cerrar clic en el fondo (modal)
modalTarea.addEventListener("click", cerrarModalTarea);

// evitar cierre al clickear el contenido
modalContentTarea.addEventListener("click", e => {
  e.stopPropagation();
});


/* ================= FETCH ================= */
async function fetchTareas() {
  const res = await fetch(`${API}/proyectos/${proyectoId}/tareas`, {
    headers: authHeaders()
  });
  return res.ok ? res.json() : [];
}

async function fetchRecursos() {
  const res = await fetch(`${API}/recursos`, {
    headers: authHeaders()
  });
  return res.ok ? res.json() : [];
}

/* ================= RENDER ================= */
function renderTareas(listId, tareas) {
  const ul = document.getElementById(listId);
  ul.innerHTML = "";

  tareas.forEach(t => {
    const li = document.createElement("li");
    li.className = "tarea-card";
    li.innerHTML = `
      <strong>${t.titulo}</strong><br>
      <span>${t.descripcion || "-"}</span><br>
      <small>${t.fecha || ""}</small><br>
      <em>Recursos: ${
        t.recursos?.length
          ? t.recursos.map(r => r.nombre).join(", ")
          : "—"
      }</em>
      <div class="actions">
        <button class="btn-delete" data-id="${t.id}">Eliminar</button>
      </div>
    `;
    ul.appendChild(li);
  });
}

function renderRecursos(recursos) {
  const select = document.getElementById("tareaRecursos");
  select.innerHTML = "";

  recursos.forEach(r => {
    const opt = document.createElement("option");
    opt.value = r.id;
    opt.textContent = `${r.nombre} (${r.cantidad} ${r.unidad})`;
    select.appendChild(opt);
  });
}

/* ================= DELETE ================= */
function attachDeleteHandlers() {
  document.querySelectorAll(".btn-delete").forEach(btn => {
    btn.onclick = async () => {
      if (!confirm("¿Eliminar tarea?")) return;

      await fetch(`${API}/tareas/${btn.dataset.id}`, {
        method: "DELETE",
        headers: authHeaders()
      });

      refreshAll();
    };
  });
}

/* ================= CREATE TAREA ================= */
document.getElementById("formTarea").addEventListener("submit", async e => {
  e.preventDefault();

  const titulo = tareaTitulo.value.trim();
  const descripcion = tareaDescripcion.value.trim();
  const fecha = tareaFecha.value;

  if (!titulo || !fecha) {
    alert("Completá título y fecha");
    return;
  }

  const res = await fetch(`${API}/tareas/proyecto/${proyectoId}`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ titulo, descripcion, fecha })
  });

  if (!res.ok) {
    alert("Error al crear tarea");
    return;
  }

  e.target.reset();
  cerrarModalTarea();
  refreshAll();
});

/* ================= OVERVIEW ================= */
function updateStats(tareas) {
  renderTareas(
    "listaTareasCreadas",
    tareas.filter(t => t.estado === "PENDIENTE")
  );
  renderTareas(
    "listaTareasEnProceso",
    tareas.filter(t => t.estado === "EN_PROCESO")
  );
  renderTareas(
    "listaTareasTerminadas",
    tareas.filter(t => t.estado === "TERMINADA")
  );

  attachDeleteHandlers();

  if (tareas.length === 0) abrirModalTarea();
}

/* ================= REFRESH ================= */
async function refreshAll() {
  const [tareas, recursos] = await Promise.all([
    fetchTareas(),
    fetchRecursos()
  ]);

  renderRecursos(recursos);
  updateStats(tareas);
}

/* ================= INIT ================= */
(() => {
  tareaFecha.value = new Date().toISOString().slice(0, 10);
  refreshAll();
})();
