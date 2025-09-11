import type { Route } from "./+types/$id";
import { Link, useParams } from "react-router";

export function meta({ params }: Route.MetaArgs) {
  return [
    { title: `URL ${params.id} - Short URL` },
    { name: "description", content: "Detalles y estadísticas de la URL" },
  ];
}

export default function UrlDetails() {
  const params = useParams();
  
  // Mock data - esto vendrá de tu API Spring Boot
  const urlData = {
    id: params.id,
    shortUrl: "https://short.ly/abc123",
    originalUrl: "https://www.ejemplo.com/una-url-muy-larga-que-necesita-ser-acortada",
    title: "Página de ejemplo",
    description: "Una página de ejemplo para demostrar el acortador de URLs",
    clicks: 124,
    createdAt: "2024-01-15T10:30:00Z",
    isActive: true,
    customSlug: "abc123",
  };

  // Mock analytics data
  const analyticsData = {
    totalClicks: 124,
    uniqueClicks: 89,
    clicksToday: 12,
    clicksThisWeek: 45,
    topCountries: [
      { country: "España", clicks: 45 },
      { country: "México", clicks: 32 },
      { country: "Argentina", clicks: 28 },
    ],
    topDevices: [
      { device: "Mobile", clicks: 67 },
      { device: "Desktop", clicks: 45 },
      { device: "Tablet", clicks: 12 },
    ],
    recentClicks: [
      { date: "2024-01-15", clicks: 12 },
      { date: "2024-01-14", clicks: 18 },
      { date: "2024-01-13", clicks: 15 },
      { date: "2024-01-12", clicks: 22 },
      { date: "2024-01-11", clicks: 8 },
    ],
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <Link
            to="/dashboard/urls"
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            ← Volver a mis URLs
          </Link>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">{urlData.title}</h1>
            <p className="text-gray-600">Detalles y estadísticas de la URL</p>
          </div>
        </div>
        <div className="flex items-center space-x-3">
          <button
            onClick={() => navigator.clipboard.writeText(urlData.shortUrl)}
            className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
          >
            📋 Copiar URL
          </button>
          <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
            ✏️ Editar
          </button>
        </div>
      </div>

      {/* URL Info */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div>
            <h3 className="text-lg font-medium text-gray-900 mb-4">Información de la URL</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">URL Acortada</label>
                <p className="mt-1 text-sm text-blue-600 font-mono bg-blue-50 p-2 rounded">
                  {urlData.shortUrl}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">URL Original</label>
                <p className="mt-1 text-sm text-gray-900 bg-gray-50 p-2 rounded break-all">
                  {urlData.originalUrl}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Descripción</label>
                <p className="mt-1 text-sm text-gray-900">
                  {urlData.description || "Sin descripción"}
                </p>
              </div>
            </div>
          </div>
          
          <div>
            <h3 className="text-lg font-medium text-gray-900 mb-4">Configuración</h3>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-700">Estado</span>
                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                  urlData.isActive 
                    ? 'bg-green-100 text-green-800' 
                    : 'bg-red-100 text-red-800'
                }`}>
                  {urlData.isActive ? 'Activa' : 'Inactiva'}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-700">Slug personalizado</span>
                <span className="text-sm font-mono text-gray-900">{urlData.customSlug}</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-700">Fecha de creación</span>
                <span className="text-sm text-gray-900">
                  {new Date(urlData.createdAt).toLocaleDateString()}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Stats Overview */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                <span className="text-blue-600 text-lg">👆</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Clics Totales</p>
              <p className="text-2xl font-semibold text-gray-900">{analyticsData.totalClicks}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                <span className="text-green-600 text-lg">👥</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Clics Únicos</p>
              <p className="text-2xl font-semibold text-gray-900">{analyticsData.uniqueClicks}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-purple-100 rounded-lg flex items-center justify-center">
                <span className="text-purple-600 text-lg">📅</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Hoy</p>
              <p className="text-2xl font-semibold text-gray-900">{analyticsData.clicksToday}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-yellow-100 rounded-lg flex items-center justify-center">
                <span className="text-yellow-600 text-lg">📊</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Esta Semana</p>
              <p className="text-2xl font-semibold text-gray-900">{analyticsData.clicksThisWeek}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Analytics */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Top Countries */}
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Países Principales</h3>
          <div className="space-y-3">
            {analyticsData.topCountries.map((item, index) => (
              <div key={index} className="flex items-center justify-between">
                <span className="text-sm text-gray-900">{item.country}</span>
                <div className="flex items-center space-x-2">
                  <div className="w-20 bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-blue-600 h-2 rounded-full" 
                      style={{ width: `${(item.clicks / analyticsData.totalClicks) * 100}%` }}
                    ></div>
                  </div>
                  <span className="text-sm font-medium text-gray-900 w-8">{item.clicks}</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Top Devices */}
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Dispositivos</h3>
          <div className="space-y-3">
            {analyticsData.topDevices.map((item, index) => (
              <div key={index} className="flex items-center justify-between">
                <span className="text-sm text-gray-900">{item.device}</span>
                <div className="flex items-center space-x-2">
                  <div className="w-20 bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-green-600 h-2 rounded-full" 
                      style={{ width: `${(item.clicks / analyticsData.totalClicks) * 100}%` }}
                    ></div>
                  </div>
                  <span className="text-sm font-medium text-gray-900 w-8">{item.clicks}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Recent Activity */}
      <div className="bg-white rounded-lg shadow p-6">
        <h3 className="text-lg font-medium text-gray-900 mb-4">Actividad Reciente</h3>
        <div className="space-y-3">
          {analyticsData.recentClicks.map((item, index) => (
            <div key={index} className="flex items-center justify-between py-2 border-b border-gray-100 last:border-b-0">
              <span className="text-sm text-gray-900">{item.date}</span>
              <span className="text-sm font-medium text-gray-900">{item.clicks} clics</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
