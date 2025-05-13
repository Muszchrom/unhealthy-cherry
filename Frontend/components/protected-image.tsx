"use client";

import Image from "next/image";
import { useEffect, useState } from "react";
import { toast } from "sonner";

export default function ProtectedImage({url, bearer}: {url: string, bearer: string}) {
  const [image, setImage] = useState("");
  useEffect(() => {
    (async () => {
      const res = await fetch(url, {
        method: "GET",
        headers: {
          "Authorization": bearer
        }
      })
      
      if (res.status === 500) {
        toast.warning(`Image not found ${res.url}`);
        return;
      }

      const blob = await res.blob();
      const objectUrl = URL.createObjectURL(blob);
      setImage(objectUrl);
    })();

    return () => {
      URL.revokeObjectURL(image);
    }
  }, [])

  return (
    <Image src={image ? image : "/Image-not-found.png"} alt="" fill className="object-cover"/>
  )
}