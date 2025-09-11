# Short URL - Frontend Admin

Una aplicación moderna de acortador de URLs construida con React Router 7, diseñada para gestionar y analizar enlaces acortados.

## 🚀 Estructura del Proyecto

La aplicación está organizada con un sistema de enrutamiento basado en archivos (file-based routing) de React Router 7:

```
app/
├── routes/
│   ├── _index.tsx                    # 🏠 Landing page promocional
│   ├── auth/
│   │   ├── login.tsx                 # 🔐 Página de inicio de sesión
│   │   └── register.tsx              # 📝 Página de registro
│   └── dashboard/
│       ├── _layout.tsx               # 📋 Layout del dashboard (sidebar + header)
│       ├── _index.tsx                # 📊 Dashboard principal
│       ├── analytics.tsx             # 📈 Analíticas globales
│       ├── settings.tsx              # ⚙️ Configuración de usuario
│       └── urls/
│           ├── _index.tsx            # 🔗 Lista de URLs
│           ├── new.tsx               # ➕ Crear nueva URL
│           └── $id.tsx               # 👁️ Detalles de URL específica
├── components/                       # 🧩 Componentes reutilizables
├── utils/                           # 🛠️ Utilidades
└── styles/                          # 🎨 Estilos
```

## � Páginas Principales

### 🌟 Landing Page (`/`)
- Página promocional con hero section
- Demostración del acortador
- Llamadas a la acción para registro/login
- Secciones de características y beneficios

### 🔐 Autenticación
- **Login** (`/login`): Formulario de inicio de sesión con opciones sociales
- **Registro** (`/register`): Formulario de registro con validaciones

### 📊 Dashboard (`/dashboard`)
- **Principal**: Resumen de estadísticas y acciones rápidas
- **URLs** (`/dashboard/urls`): Gestión completa de URLs
  - Lista con filtros y búsqueda
  - Crear nueva URL (`/dashboard/urls/new`)
  - Detalles y estadísticas (`/dashboard/urls/:id`)
- **Analíticas** (`/dashboard/analytics`): Estadísticas globales y gráficos
- **Configuración** (`/dashboard/settings`): Perfil, preferencias y seguridad

## 🎨 Características de Diseño

- ✨ **Interfaz moderna** con gradientes y efectos de glassmorphism
- 📱 **Responsive design** optimizado para móviles y desktop
- 🌙 **Modo oscuro** (ready - solo agregar toggle)
- 🎯 **UX intuitiva** con navegación clara y acciones rápidas
- 📊 **Dashboards visuales** con gráficos y métricas

## 🔧 Tecnologías Utilizadas

- **React Router 7** - Framework y enrutamiento
- **TypeScript** - Tipado estático
- **Tailwind CSS** - Estilos utilitarios
- **Vite** - Build tool y dev server
- **pnpm** - Gestor de paquetes

## 🚀 Desarrollo

```bash
# Instalar dependencias
pnpm install

# Iniciar servidor de desarrollo
pnpm run dev

# Build para producción
pnpm run build

# Preview de producción
pnpm run preview
```

## 🔗 Integración con Backend

El frontend está preparado para integrarse con tu API Spring Boot:

### Endpoints esperados:
```
POST   /api/auth/login          # Autenticación
POST   /api/auth/register       # Registro
GET    /api/urls               # Lista de URLs
POST   /api/urls               # Crear URL
GET    /api/urls/:id           # Detalles de URL
PUT    /api/urls/:id           # Actualizar URL
DELETE /api/urls/:id           # Eliminar URL
GET    /api/analytics          # Estadísticas globales
GET    /api/analytics/:id      # Estadísticas de URL específica
```

### Configuración de CORS:
Asegúrate de configurar CORS en tu backend Spring Boot para permitir peticiones desde `http://localhost:5174`.

## 📦 Componentes Reutilizables

Próximamente se añadirán componentes reutilizables en `/app/components/`:
- `url-form.tsx` - Formulario de creación/edición de URLs
- `stats-card.tsx` - Tarjetas de estadísticas
- `chart.tsx` - Componentes de gráficos
- `table.tsx` - Tablas con filtros y paginación

## 🎯 Próximas Características

- [ ] Autenticación real con JWT
- [ ] Conexión completa con API Spring Boot
- [ ] Gráficos interactivos con Chart.js/D3
- [ ] Exportación de datos (PDF, Excel, CSV)
- [ ] Notificaciones en tiempo real
- [ ] Modo oscuro toggle
- [ ] PWA (Progressive Web App)
- [ ] Internacionalización (i18n)

## 📄 Licencia

Este proyecto es parte de un sistema de acortador de URLs más amplio.
