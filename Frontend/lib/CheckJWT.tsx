"use server";

import { cookies } from "next/headers";

export async function checkJWT() {
  return !!(cookies().get("JWT")?.value);
}