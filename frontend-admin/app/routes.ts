import { type RouteConfig, index, route, layout } from "@react-router/dev/routes";

export default [
  // Landing page pública
  index("routes/_index.tsx"),
  
  // Auth routes
  route("login", "routes/auth/login.tsx"),
  route("register", "routes/auth/register.tsx"),
  
  // Dashboard protegido con layout
  layout("routes/dashboard/_layout.tsx", [
    route("dashboard", "routes/dashboard/_index.tsx"),
    route("dashboard/urls", "routes/dashboard/urls/_index.tsx"),
    route("dashboard/urls/new", "routes/dashboard/urls/new.tsx"),
    route("dashboard/urls/:id", "routes/dashboard/urls/$id.tsx"),
    route("dashboard/analytics", "routes/dashboard/analytics.tsx"),
    route("dashboard/settings", "routes/dashboard/settings.tsx"),
  ]),
] satisfies RouteConfig;
