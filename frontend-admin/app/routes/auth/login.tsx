import type { Route } from "./+types/login";
import { Form, Link, useActionData } from "react-router";
import { redirect } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Iniciar Sesión - Short URL" },
    { name: "description", content: "Accede a tu cuenta de Short URL" },
  ];
}

export async function action({ request }: Route.ActionArgs) {
  const formData = await request.formData();
  const email = formData.get("email");
  const password = formData.get("password");

  // TODO: Implementar autenticación real
  // Por ahora, simulamos login exitoso
  if (email && password) {
    // Aquí irá la lógica de autenticación con tu backend Spring Boot
    return redirect("/dashboard");
  }

  return { error: "Email y contraseña son requeridos" };
}

export default function Login() {
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
            Iniciar Sesión
          </h2>
          <p className="mt-2 text-sm text-blue-100">
            ¿No tienes una cuenta?{" "}
            <Link
              to="/register"
              className="font-medium text-yellow-300 hover:text-yellow-200"
            >
              Regístrate aquí
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
                autoComplete="current-password"
                required
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-yellow-300 focus:border-yellow-300"
                placeholder="••••••••"
              />
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <input
                  id="remember-me"
                  name="remember-me"
                  type="checkbox"
                  className="h-4 w-4 text-yellow-400 focus:ring-yellow-300 border-gray-300 rounded"
                />
                <label htmlFor="remember-me" className="ml-2 block text-sm text-white">
                  Recordarme
                </label>
              </div>

              <div className="text-sm">
                <a href="#" className="font-medium text-yellow-300 hover:text-yellow-200">
                  ¿Olvidaste tu contraseña?
                </a>
              </div>
            </div>

            <div>
              <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-gray-900 bg-yellow-400 hover:bg-yellow-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-300 transition-colors"
              >
                Iniciar Sesión
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
                <span className="px-2 bg-transparent text-white">O continúa con</span>
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
