import type { Route } from "./+types/_index";
import { Link } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Short URL - Acorta tus enlaces fácilmente" },
    { name: "description", content: "La mejor herramienta para acortar URLs y gestionar tus enlaces con estadísticas avanzadas" },
  ];
}

export default function LandingPage() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-white-200 to-blue-600">
      {/* Header */}
      <header className="bg-white/10 backdrop-blur-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold text-white">ShortURL</h1>
            </div>
            <nav className="hidden md:flex space-x-8">
              <a href="#features" className="text-white hover:text-blue-200 transition-colors">
                Características
              </a>
              <a href="#pricing" className="text-white hover:text-blue-200 transition-colors">
                Precios
              </a>
              <a href="#contact" className="text-white hover:text-blue-200 transition-colors">
                Contacto
              </a>
            </nav>
            <div className="flex space-x-4 items-center">
              <Link
                to="/login"
                className="text-white hover:text-blue-200 transition-colors"
              >
                Iniciar Sesión
              </Link>
              <Link
                to="/register"
                className="bg-white text-blue-600 px-4 py-2 rounded-lg font-medium hover:bg-blue-50 transition-colors"
              >
                Registro
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-5xl md:text-6xl font-bold text-white mb-6">
            Acorta tus URLs
            <span className="block text-yellow-300">de forma inteligente</span>
          </h2>
          <p className="text-xl text-blue-100 mb-8 max-w-2xl mx-auto">
            Transforma enlaces largos en URLs cortas y elegantes. 
            Obtén estadísticas detalladas y gestiona todos tus enlaces desde un solo lugar.
          </p>
          
          {/* URL Shortener Demo */}
          <div className="bg-white/10 backdrop-blur-sm rounded-2xl p-8 max-w-2xl mx-auto mb-8">
            <div className="flex flex-col md:flex-row gap-4">
              <input
                type="url"
                placeholder="Pega tu URL aquí..."
                className="flex-1 px-4 py-3 rounded-lg border-0 focus:ring-2 focus:ring-yellow-300 focus:outline-none"
              />
              <button className="bg-yellow-400 text-gray-900 px-6 py-3 rounded-lg font-medium hover:bg-yellow-300 transition-colors">
                Acortar URL
              </button>
            </div>
          </div>

          <Link
            to="/register"
            className="inline-block bg-yellow-400 text-gray-900 px-8 py-4 rounded-lg text-lg font-semibold hover:bg-yellow-300 transition-colors"
          >
            Comenzar Gratis
          </Link>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="py-20 bg-white/5">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h3 className="text-3xl font-bold text-white text-center mb-12">
            ¿Por qué elegir ShortURL?
          </h3>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="bg-yellow-400 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">📊</span>
              </div>
              <h4 className="text-xl font-semibold text-white mb-2">Estadísticas Avanzadas</h4>
              <p className="text-blue-100">
                Obtén insights detallados sobre tus enlaces: clics, ubicaciones, dispositivos y más.
              </p>
            </div>
            <div className="text-center">
              <div className="bg-yellow-400 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">⚡</span>
              </div>
              <h4 className="text-xl font-semibold text-white mb-2">Súper Rápido</h4>
              <p className="text-blue-100">
                Redirecciones instantáneas con nuestra infraestructura global optimizada.
              </p>
            </div>
            <div className="text-center">
              <div className="bg-yellow-400 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">🔒</span>
              </div>
              <h4 className="text-xl font-semibold text-white mb-2">100% Seguro</h4>
              <p className="text-blue-100">
                Protección contra malware y enlaces maliciosos. Tus datos están seguros. ssd
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-900 py-12 mt-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center text-gray-400">
            <p>&copy; 2024 ShortURL. Todos los derechos reservados.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}
