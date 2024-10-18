import { Photo } from "@/interfaces/interfaces";
import { cookies } from "next/headers";

export async function Contents({category, place}: {category: string, place: string}) {
  const token = cookies().get("JWT")?.value;

  const photos_data = await fetch(`http://gateway:8081/photos/photos/${category}/${place}`, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
  const photos: Photo[] = await photos_data.json();

  return (
    <>
      {photos.map((photo) => 
        <>
          <span key={photo.id}>{photo.fileName}</span>
          <img src={`http://localhost:8081/photos/photo/${photo.fileName + "." + photo.fileExtension}`}></img>
        </>
      )}
    </>
  )
}