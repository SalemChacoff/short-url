import type { Route } from "./+types/settings";
import { Form, useActionData } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Configuración - Short URL" },
    { name: "description", content: "Configura tu cuenta y preferencias" },
  ];
}

export async function action({ request }: Route.ActionArgs) {
  const formData = await request.formData();
  const intent = formData.get("intent");
  
  if (intent === "profile") {
    // Actualizar perfil
    const name = formData.get("name");
    const email = formData.get("email");
    // TODO: Enviar a tu API Spring Boot
    return { success: "Perfil actualizado correctamente" };
  }
  
  if (intent === "preferences") {
    // Actualizar preferencias
    const defaultDomain = formData.get("defaultDomain");
    const emailNotifications = formData.get("emailNotifications");
    // TODO: Enviar a tu API Spring Boot
    return { success: "Preferencias guardadas" };
  }
  
  if (intent === "password") {
    // Cambiar contraseña
    const currentPassword = formData.get("currentPassword");
    const newPassword = formData.get("newPassword");
    const confirmPassword = formData.get("confirmPassword");
    
    if (newPassword !== confirmPassword) {
      return { error: "Las contraseñas no coinciden" };
    }
    
    // TODO: Validar contraseña actual y actualizar
    return { success: "Contraseña cambiada correctamente" };
  }
  
  return null;
}

export default function Settings() {
  const actionData = useActionData<typeof action>();

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Configuración</h1>
        <p className="text-gray-600">Gestiona tu cuenta y preferencias</p>
      </div>

      {/* Success/Error Messages */}
      {actionData?.success && (
        <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
          {actionData.success}
        </div>
      )}
      {actionData?.error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
          {actionData.error}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Navigation */}
        <div className="lg:col-span-1">
          <nav className="bg-white rounded-lg shadow p-6">
            <ul className="space-y-2">
              <li>
                <a href="#profile" className="block px-3 py-2 text-sm font-medium text-blue-600 bg-blue-50 rounded-lg">
                  👤 Perfil
                </a>
              </li>
              <li>
                <a href="#preferences" className="block px-3 py-2 text-sm font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-50 rounded-lg">
                  ⚙️ Preferencias
                </a>
              </li>
              <li>
                <a href="#security" className="block px-3 py-2 text-sm font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-50 rounded-lg">
                  🔒 Seguridad
                </a>
              </li>
              <li>
                <a href="#api" className="block px-3 py-2 text-sm font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-50 rounded-lg">
                  🔑 API
                </a>
              </li>
              <li>
                <a href="#billing" className="block px-3 py-2 text-sm font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-50 rounded-lg">
                  💳 Facturación
                </a>
              </li>
            </ul>
          </nav>
        </div>

        {/* Content */}
        <div className="lg:col-span-2 space-y-6">
          {/* Profile Section */}
          <div id="profile" className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Información del perfil</h2>
            <Form method="post">
              <input type="hidden" name="intent" value="profile" />
              <div className="space-y-4">
                <div className="flex items-center space-x-6">
                  <div className="flex-shrink-0">
                    <div className="w-20 h-20 bg-blue-600 rounded-full flex items-center justify-center">
                      <span className="text-white text-2xl font-medium">U</span>
                    </div>
                  </div>
                  <div>
                    <button className="px-4 py-2 border border-gray-300 rounded-md text-sm hover:bg-gray-50">
                      Cambiar foto
                    </button>
                    <p className="text-xs text-gray-500 mt-1">JPG, PNG hasta 10MB</p>
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                      Nombre completo
                    </label>
                    <input
                      id="name"
                      name="name"
                      type="text"
                      defaultValue="Usuario Ejemplo"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div>
                    <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                      Email
                    </label>
                    <input
                      id="email"
                      name="email"
                      type="email"
                      defaultValue="usuario@ejemplo.com"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                </div>

                <div>
                  <label htmlFor="bio" className="block text-sm font-medium text-gray-700 mb-1">
                    Biografía
                  </label>
                  <textarea
                    id="bio"
                    name="bio"
                    rows={3}
                    defaultValue="Desarrollador apasionado por la tecnología y el marketing digital."
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div className="flex justify-end">
                  <button
                    type="submit"
                    className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                  >
                    Guardar cambios
                  </button>
                </div>
              </div>
            </Form>
          </div>

          {/* Preferences Section */}
          <div id="preferences" className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Preferencias</h2>
            <Form method="post">
              <input type="hidden" name="intent" value="preferences" />
              <div className="space-y-6">
                <div>
                  <label htmlFor="defaultDomain" className="block text-sm font-medium text-gray-700 mb-1">
                    Dominio por defecto
                  </label>
                  <select
                    id="defaultDomain"
                    name="defaultDomain"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="short.ly">short.ly</option>
                    <option value="sl.ink">sl.ink</option>
                    <option value="tiny.url">tiny.url</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-3">
                    Notificaciones por email
                  </label>
                  <div className="space-y-3">
                    <label className="flex items-center">
                      <input
                        type="checkbox"
                        name="emailNotifications"
                        value="weekly-reports"
                        defaultChecked
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <span className="ml-2 text-sm text-gray-900">Reportes semanales</span>
                    </label>
                    <label className="flex items-center">
                      <input
                        type="checkbox"
                        name="emailNotifications"
                        value="security-alerts"
                        defaultChecked
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <span className="ml-2 text-sm text-gray-900">Alertas de seguridad</span>
                    </label>
                    <label className="flex items-center">
                      <input
                        type="checkbox"
                        name="emailNotifications"
                        value="product-updates"
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <span className="ml-2 text-sm text-gray-900">Actualizaciones del producto</span>
                    </label>
                  </div>
                </div>

                <div className="flex justify-end">
                  <button
                    type="submit"
                    className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                  >
                    Guardar preferencias
                  </button>
                </div>
              </div>
            </Form>
          </div>

          {/* Security Section */}
          <div id="security" className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Seguridad</h2>
            
            {/* Change Password */}
            <div className="mb-6">
              <h3 className="text-base font-medium text-gray-900 mb-3">Cambiar contraseña</h3>
              <Form method="post">
                <input type="hidden" name="intent" value="password" />
                <div className="space-y-4">
                  <div>
                    <label htmlFor="currentPassword" className="block text-sm font-medium text-gray-700 mb-1">
                      Contraseña actual
                    </label>
                    <input
                      id="currentPassword"
                      name="currentPassword"
                      type="password"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div>
                    <label htmlFor="newPassword" className="block text-sm font-medium text-gray-700 mb-1">
                      Nueva contraseña
                    </label>
                    <input
                      id="newPassword"
                      name="newPassword"
                      type="password"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div>
                    <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 mb-1">
                      Confirmar nueva contraseña
                    </label>
                    <input
                      id="confirmPassword"
                      name="confirmPassword"
                      type="password"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div className="flex justify-end">
                    <button
                      type="submit"
                      className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                    >
                      Cambiar contraseña
                    </button>
                  </div>
                </div>
              </Form>
            </div>

            {/* Two-Factor Authentication */}
            <div className="border-t pt-6">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-base font-medium text-gray-900">Autenticación de dos factores</h3>
                  <p className="text-sm text-gray-600">Agrega una capa extra de seguridad a tu cuenta</p>
                </div>
                <button className="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors">
                  Configurar
                </button>
              </div>
            </div>
          </div>

          {/* API Section */}
          <div id="api" className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">API</h2>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  API Key
                </label>
                <div className="flex items-center space-x-2">
                  <input
                    type="text"
                    value="ApiKeyExample"
                    readOnly
                    className="flex-1 px-3 py-2 border border-gray-300 rounded-md bg-gray-50 font-mono text-sm"
                  />
                  <button className="px-3 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors">
                    📋
                  </button>
                  <button className="px-3 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 transition-colors">
                    Regenerar
                  </button>
                </div>
                <p className="text-xs text-gray-500 mt-1">
                  Usa esta clave para acceder a la API de Short URL
                </p>
              </div>
              
              <div>
                <button className="text-blue-600 hover:text-blue-800 text-sm">
                  📖 Ver documentación de la API
                </button>
              </div>
            </div>
          </div>

          {/* Billing Section */}
          <div id="billing" className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Facturación</h2>
            <div className="space-y-6">
              <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="text-lg font-medium text-green-900">Plan Gratuito</h3>
                    <p className="text-sm text-green-700">1,000 URLs y 10,000 clics por mes</p>
                  </div>
                  <button className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors">
                    Actualizar plan
                  </button>
                </div>
              </div>
              
              <div>
                <h3 className="text-base font-medium text-gray-900 mb-3">Uso actual</h3>
                <div className="space-y-3">
                  <div>
                    <div className="flex justify-between text-sm">
                      <span>URLs creadas</span>
                      <span>24 / 1,000</span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2 mt-1">
                      <div className="bg-blue-600 h-2 rounded-full" style={{ width: '2.4%' }}></div>
                    </div>
                  </div>
                  
                  <div>
                    <div className="flex justify-between text-sm">
                      <span>Clics este mes</span>
                      <span>1,234 / 10,000</span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2 mt-1">
                      <div className="bg-green-600 h-2 rounded-full" style={{ width: '12.34%' }}></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
