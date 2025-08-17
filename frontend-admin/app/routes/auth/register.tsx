import type { Route } from "./+types/register";
import { Form, Link, useActionData } from "react-router";
import { redirect } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Registro - Short URL" },
    { name: "description", content: "Crea tu cuenta en Short URL" },
  ];
}

export async function action({ request }: Route.ActionArgs) {
  const formData = await request.formData();
  const name = formData.get("name");
  const email = formData.get("email");
  const password = formData.get("password");
  const confirmPassword = formData.get("confirmPassword");

  // Validaciones básicas
  if (!name || !email || !password || !confirmPassword) {
    return { error: "Todos los campos son requeridos" };
  }

  if (password !== confirmPassword) {
    return { error: "Las contraseñas no coinciden" };
  }

  // TODO: Implementar registro real con tu backend Spring Boot
  // Por ahora, simulamos registro exitoso
  return redirect("/dashboard");
}

export default function Register() {
  const actionData = useActionData<typeof action>();

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-600 to-purple-700 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        {/* Header */}
        <div className="text-center">
          <Link to="/" className="text-white text-2xl font-bold">
            ShortURL
          </Link>
          <h2 className="mt-6 text-3xl font-extrabold text-white">
            Crear Cuenta
          </h2>
          <p className="mt-2 text-sm text-blue-100">
            ¿Ya tienes una cuenta?{" "}
            <Link
              to="/login"
              className="font-medium text-yellow-300 hover:text-yellow-200"
            >
              Inicia sesión aquí
            </Link>
          </p>
        </div>

        {/* Form */}
        <div className="bg-white/10 backdrop-blur-sm rounded-lg p-8">
          <Form method="post" className="space-y-6">
            {actionData?.error && (
              <div className="bg-red-500/20 border border-red-500 text-red-100 px-4 py-3 rounded">
                {actionData.error}
              </div>
            )}

            <div>
              <label htmlFor="name" className="block text-sm font-medium text-white">
                Nombre completo
              </label>
              <input
                id="name"
                name="name"
                type="text"
                autoComplete="name"
                required
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-yellow-300 focus:border-yellow-300"
                placeholder="Tu nombre"
              />
            </div>

            <div>
              <label htmlFor="email" className="block text-sm font-medium text-white">
                Email
              </label>
              <input
                id="email"
                name="email"
                type="email"
                autoComplete="email"
                required
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-yellow-300 focus:border-yellow-300"
                placeholder="tu@email.com"
              />
            </div>

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-white">
                Contraseña
              </label>
              <input
                id="password"
                name="password"
                type="password"
                autoComplete="new-password"
                required
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-yellow-300 focus:border-yellow-300"
                placeholder="••••••••"
              />
            </div>

            <div>
              <label htmlFor="confirmPassword" className="block text-sm font-medium text-white">
                Confirmar contraseña
              </label>
              <input
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                autoComplete="new-password"
                required
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-yellow-300 focus:border-yellow-300"
                placeholder="••••••••"
              />
            </div>

            <div className="flex items-center">
              <input
                id="terms"
                name="terms"
                type="checkbox"
                required
                className="h-4 w-4 text-yellow-400 focus:ring-yellow-300 border-gray-300 rounded"
              />
              <label htmlFor="terms" className="ml-2 block text-sm text-white">
                Acepto los{" "}
                <a href="#" className="text-yellow-300 hover:text-yellow-200">
                  términos y condiciones
                </a>
              </label>
            </div>

            <div>
              <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-gray-900 bg-yellow-400 hover:bg-yellow-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-300 transition-colors"
              >
                Crear Cuenta
              </button>
            </div>
          </Form>

          {/* Divider */}
          <div className="mt-6">
            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-white/20" />
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-transparent text-white">O regístrate con</span>
              </div>
            </div>

            <div className="mt-6 grid grid-cols-2 gap-3">
              <button className="w-full inline-flex justify-center py-2 px-4 border border-white/20 rounded-md shadow-sm bg-white/5 text-sm font-medium text-white hover:bg-white/10 transition-colors">
                <span>Google</span>
              </button>
              <button className="w-full inline-flex justify-center py-2 px-4 border border-white/20 rounded-md shadow-sm bg-white/5 text-sm font-medium text-white hover:bg-white/10 transition-colors">
                <span>GitHub</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
