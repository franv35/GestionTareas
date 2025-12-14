// ========================================
// DATOS DE PROYECTOS (Simulación - después conectar con backend)
// ========================================

let proyectos = [
   
];

// ========================================
// INICIALIZACIÓN
// ========================================

document.addEventListener("DOMContentLoaded", function () {
  inicializarNavegacion();
  cargarProyectos(); 
  inicializarFormulario();
});

await fetch('http://localhost:8080/api/proyectos', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(nuevoProyecto)
});

cargarProyectos(); // vuelve a pedirlos al backend

// ========================================
// NAVEGACIÓN ENTRE SECCIONES
// ========================================

function inicializarNavegacion() {
  const navItems = document.querySelectorAll(".nav-item");
  const topbarTitle = document.getElementById("topbarTitle");

  navItems.forEach((item) => {
    item.addEventListener("click", function () {
      // Si es el botón de logout
      if (this.id === "logoutBtn") {
        if (confirm("¿Deseas cerrar sesión?")) {
          window.location.href = "login.html";
        }
        return;
      }

      // Remover active de todos
      navItems.forEach((nav) => nav.classList.remove("active"));
      this.classList.add("active");

      // Cambiar vista
      const seccion = this.dataset.section;
      cambiarSeccion(seccion);

      // Actualizar topbar
      if (seccion === "proyectos") {
        topbarTitle.textContent = "Gestión de Proyectos";
      } else if (seccion === "recursos") {
        topbarTitle.textContent = "Recursos";
      } else if (seccion === "nuevo-proyecto") {
        topbarTitle.textContent = "Crear Nuevo Proyecto";
      }
    });
  });
}

function cambiarSeccion(seccion) {
  // Ocultar todas las vistas
  document.querySelectorAll(".vista-seccion").forEach((vista) => {
    vista.classList.remove("active");
  });

  // Mostrar la vista seleccionada
  if (seccion === "proyectos") {
    document.getElementById("vistaProyectos").classList.add("active");
  } else if (seccion === "recursos") {
    document.getElementById("vistaRecursos").classList.add("active");
  } else if (seccion === "nuevo-proyecto") {
    document.getElementById("vistaNuevoProyecto").classList.add("active");
  }
}


async function cargarProyectos() {
  try {
    const response = await fetch('/api/proyectos');

    if (!response.ok) {
      throw new Error('Error al obtener proyectos');
    }

    const data = await response.json();
    proyectos = data;

    renderizarProyectos();
    actualizarEstadisticas();
  } catch (error) {
    console.error('Error al cargar proyectos:', error);
    mostrarToast("Error al cargar proyectos", "error");
  }
}
// ========================================
// RENDERIZADO DE PROYECTOS
// ========================================

function renderizarProyectos() {
  const grid = document.getElementById("proyectosGrid");
  grid.innerHTML = "";

  proyectos.forEach((proyecto) => {
    const card = crearTarjetaProyecto(proyecto);
    grid.appendChild(card);
  });
}

function crearTarjetaProyecto(proyecto) {
  const total = proyecto.pendientes + proyecto.enProceso + proyecto.terminadas;
  const progreso = total > 0 ? Math.round((proyecto.terminadas / total) * 100) : 0;

  const card = document.createElement("div");
  card.className = "proyecto-card";
  card.innerHTML = `
    <div class="proyecto-header">
      <h3>${proyecto.nombre}</h3>
      <div class="proyecto-badge">${total} tareas</div>
    </div>

    <div class="proyecto-stats">
      <div class="stat-item stat-pendientes">
        <div class="stat-icon">📋</div>
        <div class="stat-content">
          <span class="stat-nombre">Pendientes</span>
          <span class="stat-numero">${proyecto.pendientes}</span>
        </div>
      </div>

      <div class="stat-item stat-proceso">
        <div class="stat-icon">⚙️</div>
        <div class="stat-content">
          <span class="stat-nombre">En Proceso</span>
          <span class="stat-numero">${proyecto.enProceso}</span>
        </div>
      </div>

      <div class="stat-item stat-terminadas">
        <div class="stat-icon">✅</div>
        <div class="stat-content">
          <span class="stat-nombre">Terminadas</span>
          <span class="stat-numero">${proyecto.terminadas}</span>
        </div>
      </div>
    </div>

    <div class="proyecto-progreso">
      <div class="progreso-header">
        <span>Progreso</span>
        <span>${progreso}%</span>
      </div>
      <div class="progreso-barra">
        <div class="progreso-fill" style="width: ${progreso}%"></div>
      </div>
    </div>
  `;

  // Agregar evento click para ver detalles (opcional)
  card.addEventListener("click", () => {
    mostrarDetallesProyecto(proyecto);
  });

  return card;
}

function mostrarDetallesProyecto(proyecto) {
  // Placeholder para funcionalidad futura
  mostrarToast(`Ver detalles de ${proyecto.nombre}`);
}

// ========================================
// ESTADÍSTICAS GENERALES
// ========================================

function actualizarEstadisticas() {
  const totalProyectos = proyectos.length;
  const tareasEnProceso = proyectos.reduce((sum, p) => sum + p.enProceso, 0);
  const tareasCompletadas = proyectos.reduce((sum, p) => sum + p.terminadas, 0);

  document.getElementById("totalProyectos").textContent = totalProyectos;
  document.getElementById("tareasEnProceso").textContent = tareasEnProceso;
  document.getElementById("tareasCompletadas").textContent = tareasCompletadas;
}

// ========================================
// FORMULARIO DE NUEVO PROYECTO
// ========================================

function inicializarFormulario() {
  const form = document.getElementById("formNuevoProyecto");

  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const nombre = document.getElementById("nombreProyecto").value.trim();
    const descripcion = document.getElementById("descripcionProyecto").value.trim();
    const fecha = document.getElementById("fechaInicio").value;

    if (!nombre || !fecha) {
      mostrarToast("Por favor completa los campos requeridos", "error");
      return;
    }

    // Crear nuevo proyecto
    const nuevoProyecto = {
      id: proyectos.length + 1,
      nombre: nombre,
      descripcion: descripcion,
      fecha: fecha,
      pendientes: 0,
      enProceso: 0,
      terminadas: 0,
    };

    proyectos.push(nuevoProyecto);

    // Actualizar vista
    renderizarProyectos();
    actualizarEstadisticas();
    limpiarFormulario();
    mostrarToast("Proyecto creado exitosamente", "success");

    // Volver a vista de proyectos
    setTimeout(() => {
      document.querySelector('[data-section="proyectos"]').click();
    }, 1500);
  });
}

function limpiarFormulario() {
  document.getElementById("formNuevoProyecto").reset();
}

// ========================================
// TOAST (MENSAJES)
// ========================================

function mostrarToast(mensaje, tipo = "info") {
  const toast = document.getElementById("toast");
  toast.textContent = mensaje;
  toast.style.display = "block";

  // Agregar clase según tipo
  toast.className = "toast";
  if (tipo === "success") {
    toast.style.backgroundColor = "var(--verde)";
  } else if (tipo === "error") {
    toast.style.backgroundColor = "var(--rojo)";
  }

  setTimeout(() => {
    toast.style.display = "none";
  }, 3000);
}

// ========================================
// BÚSQUEDA Y FILTROS (Opcional - para futuro)
// ========================================

function filtrarProyectos(termino) {
  const proyectosFiltrados = proyectos.filter((p) =>
    p.nombre.toLowerCase().includes(termino.toLowerCase())
  );

  const grid = document.getElementById("proyectosGrid");
  grid.innerHTML = "";

  proyectosFiltrados.forEach((proyecto) => {
    const card = crearTarjetaProyecto(proyecto);
    grid.appendChild(card);
  });
}
