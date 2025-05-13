"use client";
import { useEffect, useState } from "react";
import { CategorySelect } from "./category-select";
import ImageInput from "./image-input";
import { Button } from "@/components/ui/button";
import PhotosPreview from "./photos-preview";
import { Heading3 } from "@/components/headings";
import { PlaceSelect } from "./place-select";
import { Category, Place } from "@/interfaces/interfaces";
import { API_URL_CLIENT } from "@/lib/urls";
import { LoaderCircle } from "lucide-react";

export default function AddPhotosPage({bearer}: {bearer: string}) {
  const [photos, setPhotos] = useState<File[]>([]);
  const [fileStatuses, setFileStatuses] = useState<{[key: number]: {status: string, message: string}}>({});
  const [category, setCategory] = useState<Category | undefined>(undefined);
  const [place, setPlace] = useState<Place | undefined>(undefined);

  const [uploading, setUploading] = useState(false);

  const upload = async () => {
    setUploading(true);
    const statuses: {[key: number]: {status: string, message: string}} = {}

    const res = await Promise.all(photos.map(async (photo, idx) => {
      const formData = new FormData();
      formData.append("details", JSON.stringify({place: place}));
      formData.append("file", photo);
      const res = await fetch(API_URL_CLIENT + "/photos/photos", {
        method: "POST", 
        headers: {
          "Authorization": bearer
        },
        body: formData
      });
      if (res.status === 200) {
        statuses[idx] = {
          status: "ok",
          message: "ok"
        }
      }
      if (res.status === 304) {
        statuses[idx] = {
          status: "error",
          message: "Photo already exists"
        }
      } if (res.status === 400) {
        statuses[idx] = {
          status: "error",
          message: await res.text()
        }
      } if (res.status === 413) {
        statuses[idx] = {
          status: "error",
          message: "Request entity too large"
        }
      } if (res.status === 500) {
        statuses[idx] = {
          status: "error",
          message: await res.text()
        }
      }
      // console.log(await res.text())
      setUploading(false);
      return res;
    }));

    setFileStatuses(statuses);
  }

  useEffect(() => {
    console.log("Hello")
  }, [photos])

  return (
    <>
      <ImageInput photos={photos} setPhotos={setPhotos}/>
      <div className="flex w-full justify-center">
        <div className="w-full max-w-md flex flex-col justify-between gap-8">
          <div className="flex justify-between flex-wrap gap-4">
            <div>
              <Heading3>Category</Heading3>
              <CategorySelect bearer={bearer} setCategory={setCategory}/>
            </div> 
            <div>
              <Heading3>Place</Heading3>
              <PlaceSelect bearer={bearer} category={category as Category} setPlace={setPlace}/>  
            </div> 
          </div>
          <div className="ml-auto flex items-center gap-8">
            <Button className="w-[100px] ml-auto" 
                    onClick={upload} 
                    disabled={!(!!category && !!place) || uploading}>
                      {uploading ? <LoaderCircle className="animate-spin"/> : "Upload"}
            </Button>
          </div>
        </div>
      </div>
      {!!photos.length && <PhotosPreview files={photos} fileStatuses={fileStatuses}/>}
    </>
  );
}