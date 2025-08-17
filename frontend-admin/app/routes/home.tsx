// Este archivo ya no se usa - la nueva estructura utiliza _index.tsx para la landing page
// y el dashboard está en dashboard/_index.tsx

import { redirect } from "react-router";

export async function loader() {
  // Redirigir a la nueva landing page
  return redirect("/");
}
