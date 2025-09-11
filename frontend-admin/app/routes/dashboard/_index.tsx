import type { Route } from "./+types/_index";
import { Link } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Dashboard - Short URL" },
    { name: "description", content: "Panel de control de Short URL" },
  ];
}

export default function Dashboard() {
  return (
    <div className="space-y-6">
      {/* Welcome Section */}
      <div className="bg-white rounded-lg shadow p-6">
        <h1 className="text-2xl font-bold text-gray-900 mb-2">
          ¡Bienvenido de vuelta! 👋
        </h1>
        <p className="text-gray-600">
          Aquí tienes un resumen de tu actividad reciente con Short URL.
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                <span className="text-blue-600 text-lg">🔗</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">URLs Totales</p>
              <p className="text-2xl font-semibold text-gray-900">24</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                <span className="text-green-600 text-lg">👆</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Clics Totales</p>
              <p className="text-2xl font-semibold text-gray-900">1,234</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-purple-100 rounded-lg flex items-center justify-center">
                <span className="text-purple-600 text-lg">📊</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">CTR Promedio</p>
              <p className="text-2xl font-semibold text-gray-900">12.5%</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-yellow-100 rounded-lg flex items-center justify-center">
                <span className="text-yellow-600 text-lg">📈</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Este Mes</p>
              <p className="text-2xl font-semibold text-gray-900">+15%</p>
            </div>
          </div>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Acciones Rápidas</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Link
            to="/dashboard/urls/new"
            className="flex items-center p-4 border-2 border-dashed border-gray-300 rounded-lg hover:border-blue-500 hover:bg-blue-50 transition-colors group"
          >
            <div className="flex-shrink-0 w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center group-hover:bg-blue-200">
              <span className="text-blue-600 text-xl">➕</span>
            </div>
            <div className="ml-3">
              <p className="text-sm font-medium text-gray-900">Crear Nueva URL</p>
              <p className="text-xs text-gray-500">Acorta un nuevo enlace</p>
            </div>
          </Link>

          <Link
            to="/dashboard/analytics"
            className="flex items-center p-4 border-2 border-dashed border-gray-300 rounded-lg hover:border-green-500 hover:bg-green-50 transition-colors group"
          >
            <div className="flex-shrink-0 w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center group-hover:bg-green-200">
              <span className="text-green-600 text-xl">📊</span>
            </div>
            <div className="ml-3">
              <p className="text-sm font-medium text-gray-900">Ver Analíticas</p>
              <p className="text-xs text-gray-500">Revisa tus estadísticas</p>
            </div>
          </Link>

          <Link
            to="/dashboard/urls"
            className="flex items-center p-4 border-2 border-dashed border-gray-300 rounded-lg hover:border-purple-500 hover:bg-purple-50 transition-colors group"
          >
            <div className="flex-shrink-0 w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center group-hover:bg-purple-200">
              <span className="text-purple-600 text-xl">📋</span>
            </div>
            <div className="ml-3">
              <p className="text-sm font-medium text-gray-900">Gestionar URLs</p>
              <p className="text-xs text-gray-500">Ver todas tus URLs</p>
            </div>
          </Link>
        </div>
      </div>

      {/* Recent URLs */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-gray-900">URLs Recientes</h2>
          <Link
            to="/dashboard/urls"
            className="text-sm text-blue-600 hover:text-blue-800"
          >
            Ver todas
          </Link>
        </div>
        <div className="space-y-3">
          {/* Mock data - esto vendrá de tu API */}
          <div className="flex items-center justify-between p-3 border rounded-lg">
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 truncate">
                https://short.ly/abc123
              </p>
              <p className="text-xs text-gray-500 truncate">
                https://www.ejemplo.com/una-url-muy-larga-que-necesita-ser-acortada
              </p>
            </div>
            <div className="ml-4 flex items-center space-x-2">
              <span className="text-sm text-gray-500">124 clics</span>
              <button className="text-blue-600 hover:text-blue-800">
                <span className="sr-only">Copiar</span>
                📋
              </button>
            </div>
          </div>

          <div className="flex items-center justify-between p-3 border rounded-lg">
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 truncate">
                https://short.ly/def456
              </p>
              <p className="text-xs text-gray-500 truncate">
                https://www.otro-ejemplo.com/otra-url-larga
              </p>
            </div>
            <div className="ml-4 flex items-center space-x-2">
              <span className="text-sm text-gray-500">89 clics</span>
              <button className="text-blue-600 hover:text-blue-800">
                <span className="sr-only">Copiar</span>
                📋
              </button>
            </div>
          </div>

          <div className="flex items-center justify-between p-3 border rounded-lg">
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 truncate">
                https://short.ly/ghi789
              </p>
              <p className="text-xs text-gray-500 truncate">
                https://www.tercer-ejemplo.com/url-para-acortar
              </p>
            </div>
            <div className="ml-4 flex items-center space-x-2">
              <span className="text-sm text-gray-500">56 clics</span>
              <button className="text-blue-600 hover:text-blue-800">
                <span className="sr-only">Copiar</span>
                📋
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
