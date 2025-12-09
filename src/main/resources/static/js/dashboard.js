const API = "http://localhost:8080/api";
const messageEl = document.getElementById("message");

// obtenemos el token del login
const token = localStorage.getItem("token");

// si no hay token, redirigimos al login
if (!token) {
  window.location.href = "login.html";
}

// headers con autenticación
function authHeaders() {
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
  };
}

// usuario logueado
let user = null;

// mostrar mensajes tipo toast
function showMessage(text, time = 3000) {
  messageEl.textContent = text;
  messageEl.style.display = "block";
  messageEl.style.opacity = "1";
  setTimeout(() => { messageEl.style.opacity = "0"; }, time);
  setTimeout(() => { messageEl.style.display = "none"; }, time + 200);
}

// obtener info del usuario
async function fetchUserInfo() {
  try {
    const res = await fetch(`${API}/usuarios/me`, { headers: authHeaders() });
    if (!res.ok) throw new Error("No se pudo obtener info del usuario");
    user = await res.json();
    document.getElementById("userEmail").textContent = user.email;
    console.log("Usuario cargado:", user);
  } catch (e) {
    console.error(e);
    showMessage("Error obteniendo información del usuario");
    window.location.href = "login.html";
  }
}

// logout
document.getElementById("logoutBtn").addEventListener("click", () => {
  localStorage.removeItem("token");
  window.location.href = "login.html";
});

// navegación sidebar
document.querySelectorAll(".sidebar li[data-section]").forEach(li => {
  li.addEventListener("click", () => {
    document.querySelectorAll(".sidebar li").forEach(n => n.classList.remove("active"));
    li.classList.add("active");

    const section = li.getAttribute("data-section");
    document.querySelectorAll(".page").forEach(p => p.classList.remove("visible"));

    // mostrar la página correspondiente si existe
    const page = document.getElementById(section);
    if (page) page.classList.add("visible");

    // opcional: cambiar título
    const pageTitleEl = document.getElementById("pageTitle");
    if (pageTitleEl) pageTitleEl.textContent = li.textContent;

    // aquí podrías llamar fetchGastos / fetchIngresos si las agregas
  });
});

/* ==== FETCH DATA ==== */
async function fetchRecursos() {
  try {
    const res = await fetch(`${API}/recursos`, { headers: authHeaders() });
    if (!res.ok) throw new Error("No se pudieron cargar los recursos");
    const data = await res.json();
    renderRecursos(data);
    return data;
  } catch (e) {
    console.error(e);
    showMessage("Error cargando recursos");
    return [];
  }
}

async function fetchTareasCreadas() {
  try {
    const res = await fetch(`${API}/tareas-creadas/usuario/${user.id}`, { headers: authHeaders() });
    if (!res.ok) throw new Error("No se pudieron cargar las tareas creadas");
    const data = await res.json();
    renderTareas("listaTareasCreadas", data);
    attachDeleteHandlers("listaTareasCreadas");
    return data;
  } catch (e) {
    console.error(e);
    showMessage("Error cargando tareas creadas");
    return [];
  }
}

async function fetchTareasEnProceso() {
  try {
    const res = await fetch(`${API}/tareas-en-proceso/usuario/${user.id}`, { headers: authHeaders() });
    if (!res.ok) throw new Error("No se pudieron cargar las tareas en proceso");
    const data = await res.json();
    renderTareas("listaTareasEnProceso", data);
    attachDeleteHandlers("listaTareasEnProceso");
    return data;
  } catch (e) {
    console.error(e);
    showMessage("Error cargando tareas en proceso");
    return [];
  }
}

async function fetchTareasTerminadas() {
  try {
    const res = await fetch(`${API}/tareas-terminadas/usuario/${user.id}`, { headers: authHeaders() });
    if (!res.ok) throw new Error("No se pudieron cargar las tareas terminadas");
    const data = await res.json();
    renderTareas("listaTareasTerminadas", data);
    attachDeleteHandlers("listaTareasTerminadas");
    return data;
  } catch (e) {
    console.error(e);
    showMessage("Error cargando tareas terminadas");
    return [];
  }
}

/* ==== RENDER ==== */
function renderRecursos(recursos) {
  const recursoSelect = document.getElementById("tareaRecursos");
  recursoSelect.innerHTML = "";
  recursos.forEach(r => {
    const option = document.createElement("option");
    option.value = r.id;
    option.textContent = `${r.nombre} (${r.cantidad} ${r.unidadMedida})`;
    recursoSelect.appendChild(option);
  });

  document.getElementById("listaRecursos").innerHTML = recursos
    .map(r => `<li>${r.nombre} — ${r.cantidad} ${r.unidadMedida}</li>`)
    .join("");
}

function renderTareas(listId, tareas) {
  const ul = document.getElementById(listId);
  ul.innerHTML = "";
  tareas.forEach(t => {
    const li = document.createElement("li");
    li.classList.add("tarea-card");
    li.innerHTML = `
      <strong>${t.titulo}</strong><br>
      <span>${t.descripcion || "-"}</span><br>
      <small>${t.fecha}</small><br>
      <em>Recursos: ${(t.recursos && t.recursos.length > 0) 
        ? t.recursos.map(r => r.nombre).join(", ") 
        : "—"}</em>
      <div class="actions">
        <button class="btn-delete" data-id="${t.id}">Eliminar</button>
      </div>
    `;
    ul.appendChild(li);
  });
}

/* ==== DELETE HANDLER ==== */
function attachDeleteHandlers(listId) {
  const ul = document.getElementById(listId);
  ul.querySelectorAll(".btn-delete").forEach(btn => {
    btn.addEventListener("click", async (e) => {
      const id = e.target.dataset.id;
      if (!confirm("¿Eliminar tarea?")) return;
      try {
        let endpoint = "";
        if (listId === "listaTareasCreadas") endpoint = "tareas-creadas";
        if (listId === "listaTareasEnProceso") endpoint = "tareas-en-proceso";
        if (listId === "listaTareasTerminadas") endpoint = "tareas-terminadas";

        const res = await fetch(`${API}/${endpoint}/${id}`, {
          method: "DELETE",
          headers: authHeaders()
        });
        if (!res.ok) throw new Error("No se pudo eliminar");
        showMessage("Tarea eliminada");
        await refreshAll();
      } catch (err) {
        console.error(err);
        showMessage("Error eliminando tarea");
      }
    });
  });
}

/* ==== CREATE RESOURCE ==== */
document.getElementById("btnCrearRecurso").addEventListener("click", async () => {
  const nombre = document.getElementById("recursoNombre").value.trim();
  const cantidad = parseInt(document.getElementById("recursoCantidad").value);
  const unidadMedida = document.getElementById("recursoUnidad").value.trim();

  if (!nombre || !cantidad || !unidadMedida) { 
    showMessage("Completá todos los campos"); 
    return; 
  }

  try {
    const res = await fetch(`${API}/recursos`, {
      method: "POST",
      headers: authHeaders(),
      body: JSON.stringify({ nombre, cantidad, unidadMedida })
    });
    if (!res.ok) throw new Error("Error creando recurso");
    showMessage("Recurso creado");
    document.getElementById("recursoNombre").value = "";
    document.getElementById("recursoCantidad").value = "";
    document.getElementById("recursoUnidad").value = "";
    closeModal(modalRecursos);
    await fetchRecursos();
  } catch (e) {
    console.error(e);
    showMessage("Error al crear recurso");
  }
});

/* ==== CREATE TASK ==== */
document.getElementById("formTarea").addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const titulo = document.getElementById("tareaTitulo").value.trim();
  const descripcion = document.getElementById("tareaDescripcion").value.trim();
  const fecha = document.getElementById("tareaFecha").value;
  const recursoIds = Array.from(document.getElementById("tareaRecursos").selectedOptions)
                          .map(o => Number(o.value));

  if (!titulo || !fecha) { 
    showMessage("Completá título y fecha"); 
    return; 
  }

  try {
    const res = await fetch(`${API}/tareas-creadas`, {
      method: "POST",
      headers: authHeaders(),
      body: JSON.stringify({ titulo, descripcion, fecha, usuarioId: user.id, recursoIds })
    });
    if (!res.ok) throw new Error("Error creando tarea");
    showMessage("Tarea creada");
    document.getElementById("formTarea").reset();
    closeModal(modalTarea);
    await fetchTareasCreadas();
  } catch (e) {
    console.error(e);
    showMessage("Error creando tarea");
  }
});

/* ==== REFRESH ALL ==== */
async function refreshAll() {
  const recursos = await fetchRecursos();
  const creadas = await fetchTareasCreadas();
  const enProceso = await fetchTareasEnProceso();
  const terminadas = await fetchTareasTerminadas();
  updateOverview(creadas, enProceso, terminadas);
}

/* ==== INITIAL LOAD ==== */
(async function init() {
  document.getElementById("tareaFecha").value = new Date().toISOString().slice(0, 10);
  await fetchUserInfo();
  await refreshAll();
})();

/* ==== MODAL MANAGEMENT ==== */
const modalTarea = document.getElementById('modalTarea');
const modalRecursos = document.getElementById('modalRecursos');

const openModalBtns = {
  tarea: document.querySelector('[data-section="nueva-tarea"]'),
  recursos: document.querySelector('[data-section="recursos"]')
};

const closeModalBtns = {
  tarea: document.getElementById('closeTarea'),
  recursos: document.getElementById('closeRecursos')
};

function openModal(modal) { modal.style.display = 'flex'; }
function closeModal(modal) { modal.style.display = 'none'; }

// abrir modales
openModalBtns.tarea.addEventListener('click', () => openModal(modalTarea));
openModalBtns.recursos.addEventListener('click', () => openModal(modalRecursos));

// cerrar modales
closeModalBtns.tarea.addEventListener('click', () => closeModal(modalTarea));
closeModalBtns.recursos.addEventListener('click', () => closeModal(modalRecursos));

// cerrar clic fuera del modal
window.addEventListener('click', (e) => {
  if (e.target === modalTarea) closeModal(modalTarea);
  if (e.target === modalRecursos) closeModal(modalRecursos);
});

/* ==== UPDATE OVERVIEW ==== */
function updateOverview(creadas, enProceso, terminadas) {
  document.getElementById("countCreadas").textContent = creadas.length;
  document.getElementById("countEnProceso").textContent = enProceso.length;
  document.getElementById("countTerminadas").textContent = terminadas.length;
}
