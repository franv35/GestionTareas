/* ================= CONFIG ================= */
const API = "http://localhost:8080/api";

/* ================= AUTH ================= */
const token = localStorage.getItem("token");
const usuarioId = localStorage.getItem("usuarioId");
const usuarioEmail = localStorage.getItem("usuarioEmail");
const usuarioNombre = localStorage.getItem("usuarioNombre");

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

/* ================= URL PARAMS ================= */
const params = new URLSearchParams(window.location.search);
const proyectoId = params.get("proyectoId");

if (!proyectoId) {
  alert("Proyecto no especificado");
  window.location.href = "proyectos.html";
}

/* ================= USER INFO ================= */
document.getElementById("userInfo").innerText =
  `${usuarioNombre} — ${usuarioEmail}`;

/* ================= LOGOUT ================= */
document.getElementById("logoutBtn").addEventListener("click", () => {
  localStorage.clear();
  window.location.href = "login.html";
});

/* ================= FETCH ================= */
async function fetchProyecto() {
  const res = await fetch(`${API}/proyectos/${proyectoId}`, {
    headers: authHeaders()
  });
  if (!res.ok) throw new Error("Proyecto no encontrado");
  return res.json();
}

async function fetchTareas() {
  const res = await fetch(`${API}/proyectos/${proyectoId}/tareas`, {
    headers: authHeaders()
  });
  if (!res.ok) return [];
  return res.json();
}

async function fetchRecursos() {
  const res = await fetch(`${API}/recursos`, {
    headers: authHeaders()
  });
  if (!res.ok) return [];
  return res.json();
}

/* ================= RENDER ================= */
function renderProyecto(proyecto) {
  document.getElementById("tituloProyecto").innerText = proyecto.nombre;
  document.getElementById("descripcionProyecto").innerText =
    proyecto.descripcion || "Sin descripción";
}

function renderRecursos(recursos) {
  const select = document.getElementById("tareaRecursos");
  select.innerHTML = "";

  recursos.forEach(r => {
    const opt = document.createElement("option");
    opt.value = r.id;
    opt.textContent = `${r.nombre} (${r.cantidad} ${r.unidadMedida})`;
    select.appendChild(opt);
  });
}

function renderTareas(listId, tareas) {
  const ul = document.getElementById(listId);
  ul.innerHTML = "";

  tareas.forEach(t => {
    const li = document.createElement("li");
    li.className = "tarea-card";
    li.innerHTML = `
      <strong>${t.titulo}</strong><br>
      <span>${t.descripcion || "-"}</span><br>
      <small>${t.fecha}</small><br>
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

/* ================= DELETE ================= */
function attachDeleteHandlers() {
  document.querySelectorAll(".btn-delete").forEach(btn => {
    btn.onclick = async () => {
      if (!confirm("¿Eliminar tarea?")) return;

      await fetch(`${API}/tareas/${btn.dataset.id}`, {
        method: "DELETE",
        headers: authHeaders()
      });

      await refreshAll();
    };
  });
}

/* ================= CREATE TAREA ================= */
document.getElementById("formTarea").addEventListener("submit", async e => {
  e.preventDefault();

  const titulo = tareaTitulo.value.trim();
  const descripcion = tareaDescripcion.value.trim();
  const fecha = tareaFecha.value;
  const recursoIds = Array.from(tareaRecursos.selectedOptions).map(o =>
    Number(o.value)
  );

  if (!titulo || !fecha) {
    alert("Completá título y fecha");
    return;
  }

  await fetch(`${API}/tareas`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({
      titulo,
      descripcion,
      fecha,
      proyectoId,
      usuarioId,
      recursoIds
    })
  });

  e.target.reset();
  await refreshAll();
});

/* ================= OVERVIEW ================= */
function updateStats(tareas) {
  const creadas = tareas.filter(t => t.estado === "CREADA");
  const proceso = tareas.filter(t => t.estado === "EN_PROCESO");
  const terminadas = tareas.filter(t => t.estado === "TERMINADA");

  statCreadas.innerText = creadas.length;
  statProceso.innerText = proceso.length;
  statTerminadas.innerText = terminadas.length;

  renderTareas("listaTareasCreadas", creadas);
  renderTareas("listaTareasEnProceso", proceso);
  renderTareas("listaTareasTerminadas", terminadas);

  attachDeleteHandlers();
}

/* ================= REFRESH ================= */
async function refreshAll() {
  const [proyecto, tareas, recursos] = await Promise.all([
    fetchProyecto(),
    fetchTareas(),
    fetchRecursos()
  ]);

  renderProyecto(proyecto);
  renderRecursos(recursos);
  updateStats(tareas);
}

/* ================= INIT ================= */
(async function init() {
  tareaFecha.value = new Date().toISOString().slice(0, 10);
  await refreshAll();
})();
