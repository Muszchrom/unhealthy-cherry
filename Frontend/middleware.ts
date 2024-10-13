import { NextResponse, type NextRequest } from "next/server";

export function middleware(request: NextRequest) {
  const token = request.cookies.get("JWT")?.value;

  if (!token && !request.nextUrl.pathname.startsWith("/login")) {
    return Response.redirect(new URL("/login", request.url));
  }

  if (token && request.nextUrl.pathname.startsWith("/login")) {
    return Response.redirect(new URL("/", request.url));
  }
  return NextResponse.next();
}

export const config = {
   /* https://nextjs.org/docs/app/building-your-application/routing/middleware
     * Match all request paths except for the ones starting with:
     * - api (API routes)
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico (favicon file)
     */
  matcher: ['/((?!api|_next/static|_next/image|.*\\.png$).*)'],
}