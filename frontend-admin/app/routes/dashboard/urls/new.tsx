import type { Route } from "./+types/new";
import { Form, Link, useActionData } from "react-router";
import { redirect } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Nueva URL - Short URL" },
    { name: "description", content: "Crea una nueva URL acortada" },
  ];
}

export async function action({ request }: Route.ActionArgs) {
  const formData = await request.formData();
  const originalUrl = formData.get("originalUrl");
  const customSlug = formData.get("customSlug");
  const title = formData.get("title");
  const description = formData.get("description");

  // Validaciones básicas
  if (!originalUrl) {
    return { error: "La URL original es requerida" };
  }

  try {
    new URL(originalUrl as string);
  } catch {
    return { error: "Por favor ingresa una URL válida" };
  }

  // TODO: Enviar datos a tu API Spring Boot
  // const response = await fetch("/api/urls", {
  //   method: "POST",
  //   headers: { "Content-Type": "application/json" },
  //   body: JSON.stringify({ originalUrl, customSlug, title, description })
  // });

  // Por ahora, simulamos éxito
  return redirect("/dashboard/urls");
}

export default function NewUrl() {
  const actionData = useActionData<typeof action>();

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center space-x-4">
        <Link
          to="/dashboard/urls"
          className="text-gray-400 hover:text-gray-600 transition-colors"
        >
          ← Volver a mis URLs
        </Link>
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Nueva URL</h1>
          <p className="text-gray-600">Crea y personaliza tu nueva URL acortada</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Form */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg shadow p-6">
            <Form method="post" className="space-y-6">
              {actionData?.error && (
                <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                  {actionData.error}
                </div>
              )}

              {/* URL Original */}
              <div>
                <label htmlFor="originalUrl" className="block text-sm font-medium text-gray-700 mb-2">
                  URL Original *
                </label>
                <input
                  id="originalUrl"
                  name="originalUrl"
                  type="url"
                  required
                  placeholder="https://ejemplo.com/mi-url-muy-larga"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
                <p className="mt-1 text-xs text-gray-500">
                  Pega aquí la URL que quieres acortar
                </p>
              </div>

              {/* Slug Personalizado */}
              <div>
                <label htmlFor="customSlug" className="block text-sm font-medium text-gray-700 mb-2">
                  Slug Personalizado (Opcional)
                </label>
                <div className="flex items-center">
                  <span className="bg-gray-50 border border-r-0 border-gray-300 rounded-l-md px-3 py-2 text-gray-500 text-sm">
                    short.ly/
                  </span>
                  <input
                    id="customSlug"
                    name="customSlug"
                    type="text"
                    placeholder="mi-enlace-personalizado"
                    className="flex-1 px-3 py-2 border border-gray-300 rounded-r-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <p className="mt-1 text-xs text-gray-500">
                  Si no especificas uno, se generará automáticamente
                </p>
              </div>

              {/* Título */}
              <div>
                <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-2">
                  Título (Opcional)
                </label>
                <input
                  id="title"
                  name="title"
                  type="text"
                  placeholder="Mi página web"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
                <p className="mt-1 text-xs text-gray-500">
                  Un título descriptivo para identificar fácilmente esta URL
                </p>
              </div>

              {/* Descripción */}
              <div>
                <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-2">
                  Descripción (Opcional)
                </label>
                <textarea
                  id="description"
                  name="description"
                  rows={3}
                  placeholder="Descripción adicional sobre esta URL..."
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              {/* Botones */}
              <div className="flex items-center justify-end space-x-4">
                <Link
                  to="/dashboard/urls"
                  className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
                >
                  Cancelar
                </Link>
                <button
                  type="submit"
                  className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                >
                  Crear URL
                </button>
              </div>
            </Form>
          </div>
        </div>

        {/* Preview/Info Panel */}
        <div className="space-y-6">
          {/* Preview */}
          <div className="bg-white rounded-lg shadow p-6">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Vista Previa</h3>
            <div className="space-y-3">
              <div className="p-3 border rounded-lg bg-gray-50">
                <p className="text-sm font-medium text-gray-900">URL Acortada</p>
                <p className="text-sm text-blue-600 font-mono">short.ly/abc123</p>
              </div>
              <div className="p-3 border rounded-lg">
                <p className="text-sm font-medium text-gray-900">URL Original</p>
                <p className="text-xs text-gray-500 break-all">
                  Se mostrará aquí cuando ingreses una URL
                </p>
              </div>
            </div>
          </div>

          {/* Tips */}
          <div className="bg-blue-50 rounded-lg p-6">
            <h3 className="text-lg font-medium text-blue-900 mb-3">💡 Consejos</h3>
            <ul className="space-y-2 text-sm text-blue-800">
              <li>• Usa slugs descriptivos para URLs más memorables</li>
              <li>• Agrega títulos para organizar mejor tus enlaces</li>
              <li>• Las URLs personalizadas no se pueden cambiar después</li>
              <li>• Todas las URLs se verifican automáticamente por seguridad</li>
            </ul>
          </div>

          {/* Stats */}
          <div className="bg-white rounded-lg shadow p-6">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Tu Actividad</h3>
            <div className="space-y-3">
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">URLs creadas</span>
                <span className="text-sm font-medium">24</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">Clics totales</span>
                <span className="text-sm font-medium">1,234</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">Este mes</span>
                <span className="text-sm font-medium text-green-600">+15%</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
