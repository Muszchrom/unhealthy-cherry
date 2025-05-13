// import { signOut } from "next-auth/react";
// import { NextRequest, NextResponse } from "next/server";

export { default } from "next-auth/middleware"
export const config = {matcher: [
  '/((?!api|_next/static|_next/image|favicon.ico|sitemap.xml|robots.txt|login).*)'
]}
// export function middleware(request: NextRequest) {
//   const token = request.cookies.get("next-auth.session-token")?.value;
//   // if (!token) {
//   //   return Response.redirect(new URL("/login", request.url));
//   // }
//   // return NextResponse.next();
// }