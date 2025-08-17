import type { Route } from "./+types/_index";
import { Link } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Mis URLs - Short URL" },
    { name: "description", content: "Gestiona todas tus URLs acortadas" },
  ];
}

export default function UrlsList() {
  // Mock data - esto vendrá de tu API Spring Boot
  const urls = [
    {
      id: "1",
      shortUrl: "https://short.ly/abc123",
      originalUrl: "https://www.ejemplo.com/una-url-muy-larga-que-necesita-ser-acortada",
      title: "Página de ejemplo",
      clicks: 124,
      createdAt: "2024-01-15",
      isActive: true,
    },
    {
      id: "2",
      shortUrl: "https://short.ly/def456",
      originalUrl: "https://www.otro-ejemplo.com/otra-url-larga",
      title: "Otro ejemplo",
      clicks: 89,
      createdAt: "2024-01-14",
      isActive: true,
    },
    {
      id: "3",
      shortUrl: "https://short.ly/ghi789",
      originalUrl: "https://www.tercer-ejemplo.com/url-para-acortar",
      title: "Tercer ejemplo",
      clicks: 56,
      createdAt: "2024-01-13",
      isActive: false,
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Mis URLs</h1>
          <p className="text-gray-600">Gestiona y monitorea todas tus URLs acortadas</p>
        </div>
        <Link
          to="/dashboard/urls/new"
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
        >
          Nueva URL
        </Link>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex items-center space-x-4">
          <input
            type="text"
            placeholder="Buscar URLs..."
            className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <select className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>Todas</option>
            <option>Activas</option>
            <option>Inactivas</option>
          </select>
          <select className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>Más recientes</option>
            <option>Más clics</option>
            <option>Menos clics</option>
          </select>
        </div>
      </div>

      {/* URLs List */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h2 className="text-lg font-medium text-gray-900">
            {urls.length} URLs encontradas
          </h2>
        </div>
        
        <div className="divide-y divide-gray-200">
          {urls.map((url) => (
            <div key={url.id} className="px-6 py-4 hover:bg-gray-50">
              <div className="flex items-center justify-between">
                <div className="flex-1 min-w-0">
                  <div className="flex items-center space-x-3">
                    <div className={`w-3 h-3 rounded-full ${url.isActive ? 'bg-green-400' : 'bg-gray-400'}`}></div>
                    <div className="flex-1 min-w-0">
                      <h3 className="text-sm font-medium text-gray-900 truncate">
                        {url.title}
                      </h3>
                      <div className="mt-1 space-y-1">
                        <p className="text-sm text-blue-600 font-mono">
                          {url.shortUrl}
                        </p>
                        <p className="text-xs text-gray-500 truncate">
                          {url.originalUrl}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
                
                <div className="ml-4 flex items-center space-x-4">
                  <div className="text-center">
                    <p className="text-sm font-medium text-gray-900">{url.clicks}</p>
                    <p className="text-xs text-gray-500">clics</p>
                  </div>
                  
                  <div className="text-center">
                    <p className="text-xs text-gray-500">{url.createdAt}</p>
                  </div>
                  
                  <div className="flex items-center space-x-2">
                    <button
                      onClick={() => navigator.clipboard.writeText(url.shortUrl)}
                      className="text-gray-400 hover:text-blue-600 transition-colors"
                      title="Copiar URL"
                    >
                      📋
                    </button>
                    <Link
                      to={`/dashboard/urls/${url.id}`}
                      className="text-gray-400 hover:text-blue-600 transition-colors"
                      title="Ver detalles"
                    >
                      👁️
                    </Link>
                    <button
                      className="text-gray-400 hover:text-red-600 transition-colors"
                      title="Eliminar"
                    >
                      🗑️
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Pagination */}
      <div className="bg-white rounded-lg shadow p-4">
        <div className="flex items-center justify-between">
          <p className="text-sm text-gray-700">
            Mostrando <span className="font-medium">1</span> a <span className="font-medium">3</span> de{' '}
            <span className="font-medium">3</span> resultados
          </p>
          <div className="flex items-center space-x-2">
            <button className="px-3 py-1 border border-gray-300 rounded text-sm text-gray-500 cursor-not-allowed">
              Anterior
            </button>
            <button className="px-3 py-1 bg-blue-600 text-white rounded text-sm">
              1
            </button>
            <button className="px-3 py-1 border border-gray-300 rounded text-sm text-gray-500 cursor-not-allowed">
              Siguiente
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
