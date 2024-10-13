"use client";

import { useRouter } from "next/navigation";
import { useEffect } from "react";
import { toast } from "sonner";

export default function LogOut() {
  const router = useRouter();

  useEffect(() => {
    (async () => {
      fetch("http://localhost:8081/auth/logout", {
        method: "GET",
        credentials: "include"
      }).then(res => {
        if (res.status === 200) {
          router.push("/login");
        } else {
          toast.error("coś poszło nie tak", {
            description: "Kod błędu: " + res.status
          })
        }
      });
    })()
  }, [])

  return(
    <main className="flex-[1_1_0]">
      <div className="border-b container flex justify-center items-center h-full">
        <div className="text-center">
          Wylogowywanie ...
        </div>
      </div>
    </main>
  );
}