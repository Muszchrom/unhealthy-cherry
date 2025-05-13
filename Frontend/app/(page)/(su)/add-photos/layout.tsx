import { authOptions } from "@/app/api/auth/[...nextauth]/route";
import { getServerSession } from "next-auth";
import React from "react";
import AddPhotosPage from "./page";
import { Jumbo } from "@/components/jumbo";

export default async function AddPhotos({children}: {children: React.ReactNode}) {
  const session = await getServerSession(authOptions);
  if (!session) return;

  return (
    <>
      <Jumbo description="Upload new photos to the server">
        Add photos
      </Jumbo>
      <AddPhotosPage bearer={"Bearer " + session?.user.APIToken}/>
    </>
  );
}