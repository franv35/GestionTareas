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
    Authorization: `Bearer ${token}`
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
const userInfoEl = document.getElementById("userEmail");
const logoutBtn = document.getElementById("logoutBtn");

const modalTarea = document.getElementById("modalTarea");
const modalContentTarea = modalTarea.querySelector(".modal-content");
const closeTarea = document.getElementById("closeTarea");

const formTarea = document.getElementById("formTarea");

const listaPendientes = document.getElementById("listaTareasCreadas");
const listaEnProceso = document.getElementById("listaTareasEnProceso");
const listaTerminadas = document.getElementById("listaTareasTerminadas");

const selectRecursos = document.getElementById("tareaRecursos");

/* ======================================================
   USER INFO
====================================================== */
if (userInfoEl) {
  userInfoEl.innerText = usuarioEmail || "—";
}

/* ======================================================
   LOGOUT
====================================================== */
logoutBtn?.addEventListener("click", () => {
  localStorage.clear();
  window.location.href = "login.html";
});

/* ======================================================
   MODAL TAREA
====================================================== */
function abrirModalTarea() {
  modalTarea.classList.add("open");
}

function cerrarModalTarea() {
  modalTarea.classList.remove("open");
}

// Abrir desde sidebar
document
  .querySelector('[data-section="nueva-tarea"]')
  ?.addEventListener("click", abrirModalTarea);

// Cerrar con X
closeTarea?.addEventListener("click", e => {
  e.stopPropagation();
  cerrarModalTarea();
});

// Cerrar clic en fondo
modalTarea?.addEventListener("click", cerrarModalTarea);

// Evitar cierre al clickear contenido
modalContentTarea?.addEventListener("click", e => {
  e.stopPropagation();
});

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
    const res = await fetch(`${API}/recursos`, {
      headers: authHeaders()
    });
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
	  	${t.estado === "PENDIENTE" 
	  	  ? `<button class="btn-move" data-id="${t.id}" data-estado="EN_PROGRESO">
	  	       Iniciar
	 	      </button>`
	 	   : ""
	 	 }

	 	 ${t.estado === "EN_PROGRESO"
	 	   ? `<button class="btn-complete" data-id="${t.id}" data-estado="COMPLETADA">
	  	       Finalizar
	  	     </button>`
	  	  : ""
		  }

	 	 ${t.estado === "EN_PROGRESO"
	  	  ? `<button class="btn-move" data-id="${t.id}" data-estado="PENDIENTE">
	    	     Volver
	   	    </button>`
	  	  : ""
	 	 }

        <button class="btn-delete" data-id="${t.id}">
          Eliminar
        </button>
      </div>
    `;

    listaEl.appendChild(li);
  });
}

/* ======================================================
   RENDER RECURSOS
====================================================== */
function renderRecursos(recursos) {
  selectRecursos.innerHTML = "";

  if (!recursos.length) {
    const opt = document.createElement("option");
    opt.textContent = "No hay recursos creados";
    opt.disabled = true;
    opt.selected = true;
    selectRecursos.appendChild(opt);
    return;
  }

  recursos.forEach(r => {
    const opt = document.createElement("option");
    opt.value = r.id;
    opt.textContent = `${r.nombre} (${r.cantidad} ${r.unidad})`;
    selectRecursos.appendChild(opt);
  });
}

/* ======================================================
   DELETE TAREA
====================================================== */
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

function attachMoveHandlers() {
  document.querySelectorAll(".btn-move, .btn-complete")
    .forEach(btn => {
      btn.onclick = async () => {
        await fetch(
          `${API}/tareas/${btn.dataset.id}/estado`,
          {
            method: "PUT",
            headers: authHeaders(),
            body: JSON.stringify({ estado: btn.dataset.estado })
          }
        );

        refreshAll();
      };
    });
}
/* ======================================================
   EMPTY STATE
====================================================== */
function renderEmptyStateSiCorresponde(tareas) {
  if (tareas.length !== 0) return;

  listaPendientes.innerHTML = `
    <div class="empty-state">
      <p>No hay tareas aún.</p>
      <button class="btn" id="btnCrearPrimeraTarea">
        Crear mi primera tarea
      </button>
    </div>
  `;

  document
    .getElementById("btnCrearPrimeraTarea")
    ?.addEventListener("click", abrirModalTarea);
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

  const res = await fetch(
    `${API}/tareas/proyecto/${proyectoId}`,
    {
      method: "POST",
      headers: authHeaders(),
      body: JSON.stringify({ titulo, descripcion, fecha })
    }
  );

  if (!res.ok) {
    alert("Error al crear tarea");
    return;
  }

  formTarea.reset();
  cerrarModalTarea();
  refreshAll();
});

/* ======================================================
   REFRESH
====================================================== */
async function refreshAll() {
  const [tareas, recursos] = await Promise.all([
	fetchTareas(),
    fetchRecursos()
  ]);

  const pendientes = tareas.filter(t => t.estado === "PENDIENTE");
  const enProceso = tareas.filter(t => t.estado === "EN_PROGRESO");
  const terminadas = tareas.filter(t => t.estado === "COMPLETADA");

  renderTareas(listaPendientes, pendientes);
  renderTareas(listaEnProceso, enProceso);
  renderTareas(listaTerminadas, terminadas);

  renderRecursos(recursos);
  attachDeleteHandlers();
  attachMoveHandlers(),
  renderEmptyStateSiCorresponde(tareas);
}

/* ======================================================
   INIT
====================================================== */
(() => {
  if (typeof tareaFecha !== "undefined") {
    tareaFecha.value = new Date().toISOString().slice(0, 10);
  }
  refreshAll();
})();
