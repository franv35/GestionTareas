const API = "http://localhost:8080/api";
const token = localStorage.getItem("token");
const usuarioEmail = localStorage.getItem("usuarioEmail");
const proyectoId = new URLSearchParams(window.location.search).get("proyectoId") || localStorage.getItem("proyectoIdActual");

if (!token) window.location.href = "login.html";

const authHeaders = () => ({
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
});

/* ======================================================
   LOGICA DE RECURSOS (ASIGNAR)
====================================================== */

async function abrirModalAsignacion(idTarea, titulo) {
    document.getElementById("tareaIdDestino").value = idTarea;
    document.getElementById("nombreTareaDestino").innerText = `Asignar a: ${titulo}`;
    document.getElementById("panelAsignarTarea").style.display = "block";
    
    const recursos = await fetchRecursos();
    const select = document.getElementById("selectRecursoAsignar");
    
    select.innerHTML = recursos.map(r => 
        `<option value="${r.id}">${r.nombre} (Disp: ${r.stockDisponible} ${r.unidad})</option>`
    ).join("");
    
    document.getElementById("modalRecursos").classList.add("open");
    renderRecursosInventario(recursos);
}

document.getElementById("btnConfirmarAsignacion").onclick = async () => {
    // Este objeto coincide con tu AsignarRecursoDTO.java
    const dto = {
        tareaId: parseInt(document.getElementById("tareaIdDestino").value),
        recursoId: parseInt(document.getElementById("selectRecursoAsignar").value),
        cantidad: parseInt(document.getElementById("cantidadAsignar").value)
    };

    const res = await fetch(`${API}/tareas-recursos`, {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify(dto)
    });

    if (res.ok) {
        alert("Recurso asignado!");
        document.getElementById("modalRecursos").classList.remove("open");
        refreshAll();
    } else {
        const err = await res.text();
        alert("Error: " + err);
    }
};

/* ======================================================
   RENDERIZADO DE COMPONENTES
====================================================== */

function renderTareas(listaEl, tareas) {
    listaEl.innerHTML = tareas.length === 0 ? '<li class="empty">Vacío</li>' : "";
    tareas.forEach(t => {
        const li = document.createElement("li");
        li.className = "tarea-card";
        li.innerHTML = `
            <strong>${t.titulo}</strong>
            <p style="font-size:0.9em; color:#666">${t.descripcion || ""}</p>
            <div style="margin: 8px 0;">
                ${t.recursos ? t.recursos.map(r => `<span class="badge-recurso">${r.nombre}</span>`).join("") : ""}
            </div>
            <div class="actions">
                ${t.estado !== 'COMPLETADA' ? `<button class="btn-recurso-add" onclick="abrirModalAsignacion(${t.id}, '${t.titulo}')">📦+</button>` : ''}
                ${t.estado === 'PENDIENTE' ? `<button onclick="cambiarEstado(${t.id}, 'EN_PROGRESO')">▶️</button>` : ''}
                ${t.estado === 'EN_PROGRESO' ? `<button onclick="cambiarEstado(${t.id}, 'COMPLETADA')">✅</button>` : ''}
                <button onclick="eliminarTarea(${t.id})" style="background:#e53e3e; color:white; border:none; padding:5px; border-radius:4px;">🗑️</button>
            </div>
        `;
        listaEl.appendChild(li);
    });
}

async function cambiarEstado(id, nuevoEstado) {
    await fetch(`${API}/tareas/${id}/estado`, {
        method: "PUT",
        headers: authHeaders(),
        body: JSON.stringify({ estado: nuevoEstado })
    });
    refreshAll();
}

async function eliminarTarea(id) {
    if (confirm("Al eliminar la tarea, los recursos volverán al stock. ¿Continuar?")) {
        await fetch(`${API}/tareas/${id}`, { method: "DELETE", headers: authHeaders() });
        refreshAll();
    }
}

/* ======================================================
   CARGA DE DATOS
====================================================== */

async function fetchRecursos() {
    const res = await fetch(`${API}/recursos/proyecto/${proyectoId}`, { headers: authHeaders() });
    return res.ok ? await res.json() : [];
}

async function fetchTareas() {
    const res = await fetch(`${API}/proyectos/${proyectoId}/tareas`, { headers: authHeaders() });
    return res.ok ? await res.json() : [];
}

async function refreshAll() {
    const [tareas, recursos] = await Promise.all([fetchTareas(), fetchRecursos()]);
    
    renderTareas(document.getElementById("listaTareasCreadas"), tareas.filter(t => t.estado === "PENDIENTE"));
    renderTareas(document.getElementById("listaTareasEnProceso"), tareas.filter(t => t.estado === "EN_PROGRESO"));
    renderTareas(document.getElementById("listaTareasTerminadas"), tareas.filter(t => t.estado === "COMPLETADA"));
    
    renderRecursosInventario(recursos);
}

function renderRecursosInventario(recursos) {
    const lista = document.getElementById("listaRecursos");
    lista.innerHTML = recursos.map(r => `
        <li style="display:flex; justify-content:space-between; padding:8px; border-bottom:1px solid #eee">
            <span>${r.nombre}</span>
            <span class="stock-info">${r.stockDisponible} / ${r.stockTotal} ${r.unidad}</span>
        </li>
    `).join("");
}

/* ======================================================
   INICIALIZACIÓN Y EVENTOS
====================================================== */

document.getElementById("openRecursos").onclick = () => {
    document.getElementById("panelAsignarTarea").style.display = "none";
    document.getElementById("modalRecursos").classList.add("open");
    refreshAll();
};

document.getElementById("btnCrearRecursoModal").onclick = async () => {
    const req = {
        nombre: document.getElementById("recursoNombre").value,
        stockTotal: parseInt(document.getElementById("recursoCantidad").value),
        unidad: document.getElementById("recursoUnidad").value,
        proyectoId: parseInt(proyectoId)
    };
    await fetch(`${API}/recursos`, { method: "POST", headers: authHeaders(), body: JSON.stringify(req) });
    refreshAll();
};

document.getElementById("formTarea").onsubmit = async (e) => {
    e.preventDefault();
    const tarea = {
        titulo: document.getElementById("tareaTitulo").value,
        descripcion: document.getElementById("tareaDescripcion").value,
        fecha: document.getElementById("tareaFecha").value,
        estado: "PENDIENTE"
    };
    await fetch(`${API}/tareas/proyecto/${proyectoId}`, { 
        method: "POST", 
        headers: authHeaders(), 
        body: JSON.stringify(tarea) 
    });
    document.getElementById("modalTarea").classList.remove("open");
    refreshAll();
};

// Cierres de modal
document.querySelectorAll(".close").forEach(c => {
    c.onclick = () => document.querySelectorAll(".modal").forEach(m => m.classList.remove("open"));
});

document.getElementById("openCrearTarea").onclick = () => document.getElementById("modalTarea").classList.add("open");
document.getElementById("logoutBtn").onclick = () => { localStorage.clear(); window.location.href="login.html"; };
document.getElementById("userEmail").innerText = usuarioEmail;

refreshAll();