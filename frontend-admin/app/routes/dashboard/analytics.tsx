import type { Route } from "./+types/analytics";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Analíticas - Short URL" },
    { name: "description", content: "Estadísticas y análisis de tus URLs" },
  ];
}

export default function Analytics() {
  // Mock data - esto vendrá de tu API Spring Boot
  const analyticsData = {
    overview: {
      totalUrls: 24,
      totalClicks: 1234,
      uniqueVisitors: 892,
      averageCtr: 12.5,
    },
    topUrls: [
      { shortUrl: "short.ly/abc123", title: "Página principal", clicks: 345 },
      { shortUrl: "short.ly/def456", title: "Producto destacado", clicks: 278 },
      { shortUrl: "short.ly/ghi789", title: "Blog post viral", clicks: 234 },
      { shortUrl: "short.ly/jkl012", title: "Campaña social", clicks: 189 },
    ],
    clicksOverTime: [
      { date: "2024-01-01", clicks: 45 },
      { date: "2024-01-02", clicks: 52 },
      { date: "2024-01-03", clicks: 38 },
      { date: "2024-01-04", clicks: 61 },
      { date: "2024-01-05", clicks: 47 },
      { date: "2024-01-06", clicks: 73 },
      { date: "2024-01-07", clicks: 65 },
    ],
    topCountries: [
      { country: "España", clicks: 456, percentage: 37 },
      { country: "México", clicks: 321, percentage: 26 },
      { country: "Argentina", clicks: 234, percentage: 19 },
      { country: "Colombia", clicks: 123, percentage: 10 },
      { country: "Chile", clicks: 100, percentage: 8 },
    ],
    devices: {
      mobile: 65,
      desktop: 28,
      tablet: 7,
    },
    browsers: [
      { name: "Chrome", percentage: 58 },
      { name: "Safari", percentage: 23 },
      { name: "Firefox", percentage: 12 },
      { name: "Edge", percentage: 7 },
    ],
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Analíticas</h1>
        <p className="text-gray-600">Estadísticas detalladas de todas tus URLs</p>
      </div>

      {/* Date Range Selector */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex items-center justify-between">
          <h2 className="text-lg font-medium text-gray-900">Rango de fechas</h2>
          <div className="flex items-center space-x-4">
            <select className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
              <option>Últimos 7 días</option>
              <option>Últimos 30 días</option>
              <option>Últimos 3 meses</option>
              <option>Último año</option>
              <option>Personalizado</option>
            </select>
            <button className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors">
              Aplicar
            </button>
          </div>
        </div>
      </div>

      {/* Overview Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                <span className="text-blue-600 text-lg">🔗</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">URLs Totales</p>
              <p className="text-2xl font-semibold text-gray-900">{analyticsData.overview.totalUrls}</p>
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
              <p className="text-2xl font-semibold text-gray-900">{analyticsData.overview.totalClicks.toLocaleString()}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-purple-100 rounded-lg flex items-center justify-center">
                <span className="text-purple-600 text-lg">👥</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Visitantes Únicos</p>
              <p className="text-2xl font-semibold text-gray-900">{analyticsData.overview.uniqueVisitors.toLocaleString()}</p>
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
              <p className="text-sm font-medium text-gray-600">CTR Promedio</p>
              <p className="text-2xl font-semibold text-gray-900">{analyticsData.overview.averageCtr}%</p>
            </div>
          </div>
        </div>
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Clicks Over Time */}
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Clics en el tiempo</h3>
          <div className="h-64 flex items-end justify-between space-x-2">
            {analyticsData.clicksOverTime.map((day, index) => (
              <div key={index} className="flex flex-col items-center">
                <div 
                  className="bg-blue-600 rounded-t"
                  style={{ 
                    height: `${(day.clicks / Math.max(...analyticsData.clicksOverTime.map(d => d.clicks))) * 200}px`,
                    width: '32px'
                  }}
                ></div>
                <span className="text-xs text-gray-500 mt-2">
                  {new Date(day.date).getDate()}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Device Distribution */}
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Distribución por dispositivos</h3>
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <span className="text-2xl">📱</span>
                <span className="text-sm text-gray-900">Móvil</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-24 bg-gray-200 rounded-full h-2">
                  <div className="bg-blue-600 h-2 rounded-full" style={{ width: `${analyticsData.devices.mobile}%` }}></div>
                </div>
                <span className="text-sm font-medium text-gray-900 w-10">{analyticsData.devices.mobile}%</span>
              </div>
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <span className="text-2xl">💻</span>
                <span className="text-sm text-gray-900">Escritorio</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-24 bg-gray-200 rounded-full h-2">
                  <div className="bg-green-600 h-2 rounded-full" style={{ width: `${analyticsData.devices.desktop}%` }}></div>
                </div>
                <span className="text-sm font-medium text-gray-900 w-10">{analyticsData.devices.desktop}%</span>
              </div>
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <span className="text-2xl">📚</span>
                <span className="text-sm text-gray-900">Tablet</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-24 bg-gray-200 rounded-full h-2">
                  <div className="bg-purple-600 h-2 rounded-full" style={{ width: `${analyticsData.devices.tablet}%` }}></div>
                </div>
                <span className="text-sm font-medium text-gray-900 w-10">{analyticsData.devices.tablet}%</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Top URLs and Countries */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Top URLs */}
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">URLs más populares</h3>
          <div className="space-y-3">
            {analyticsData.topUrls.map((url, index) => (
              <div key={index} className="flex items-center justify-between p-3 border rounded-lg hover:bg-gray-50">
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-gray-900 truncate">{url.title}</p>
                  <p className="text-xs text-blue-600 font-mono">{url.shortUrl}</p>
                </div>
                <span className="text-sm font-medium text-gray-900 ml-4">{url.clicks} clics</span>
              </div>
            ))}
          </div>
        </div>

        {/* Top Countries */}
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Países principales</h3>
          <div className="space-y-3">
            {analyticsData.topCountries.map((country, index) => (
              <div key={index} className="flex items-center justify-between">
                <span className="text-sm text-gray-900">{country.country}</span>
                <div className="flex items-center space-x-2">
                  <div className="w-20 bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-blue-600 h-2 rounded-full" 
                      style={{ width: `${country.percentage}%` }}
                    ></div>
                  </div>
                  <span className="text-sm font-medium text-gray-900 w-12">{country.clicks}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Export Section */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-lg font-medium text-gray-900">Exportar datos</h3>
            <p className="text-sm text-gray-600">Descarga tus estadísticas en diferentes formatos</p>
          </div>
          <div className="flex items-center space-x-3">
            <button className="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors">
              📊 Excel
            </button>
            <button className="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors">
              📄 CSV
            </button>
            <button className="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors">
              📋 PDF
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
