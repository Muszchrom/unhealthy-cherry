import { getServerSession } from "next-auth";
import React from "react";
import { authOptions } from "../api/auth/[...nextauth]/route";
import { redirect } from "next/navigation";
import { Heading1 } from "@/components/headings";

export default async function LoginLayout({children}: {children: React.ReactNode}) {
  const session = await getServerSession(authOptions);
  if (session) redirect("/");

  return (
    <main className="container w-full px-4 flex flex-col flex-1 gap-8 row-start-2 items-center justify-center sm:items-start">
      <Heading1 className="text-center w-full">Zaloguj się</Heading1>
      <div className="container mx-auto flex justify-center items-center h-full">
        <div className="max-w-xs w-full">
          {children}
        </div>
      </div>
    </main>
  )
}