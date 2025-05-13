"use client";
import { Heading2 } from "@/components/headings";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import { Check, LoaderCircle, TriangleAlert, X } from "lucide-react";
import { useEffect, useState } from "react";

export default function PhotosPreview({files, fileStatuses}: {files: File[], fileStatuses: {[key: number]: {status: string, message: string}}}) {
  return (
    <div className="w-full ">
      <Heading2 className="pb-4">Photos preview</Heading2>
      <ScrollArea className="h-[750px]">
        <div className="grid w-full grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4 ">
          {Array.from(files).map((file, idx) => 
            <SingleImage key={idx} file={file} idx={idx} fileStatus={fileStatuses?.[idx]}/>
          )}
        </div>
        <ScrollBar orientation="vertical"/>
      </ScrollArea>
    </div>
  );
}

function SingleImage({file, idx, fileStatus}: {file: File, idx: number, fileStatus: {status: string, message: string};}) {
  const [fileUrl, setFileUrl] = useState<string | undefined>(undefined);
  useEffect(() => {
    (async () => {
      const x = await resizeImage(file)
      setFileUrl(x)
    })()
  }, [])
  return (
    <div key={idx} className="aspect-square relative">
      <img src={fileUrl} loading="lazy" width="100" height="100" className="w-full h-full object-cover rounded-md"/>

      {fileStatus?.status === "error" && (
        <div className="absolute top-0 left-0 right-0 bottom-0 flex flex-col items-center justify-center bg-black bg-opacity-75">
          <TriangleAlert className="stroke-red-500"/>
          <span className="text-sm font-bold text-center text-red-500 w-3/4">An error occurred during upload</span>
          <span className="text-sm font-bold text-center text-red-500 w-3/4">{fileStatus.message}</span>
        </div>
      )}

      {fileStatus?.status === "ok" && (
        <div className="absolute top-0 left-0 right-0 bottom-0 flex flex-col items-center justify-center bg-black bg-opacity-75">
          <Check className="stroke-green-500"/>
          <span className="text-sm font-bold text-center text-green-500 w-3/4">Success</span>
        </div>
      )}

      {fileStatus?.status === "pending" || fileStatus?.status === "success" && (
        <div className="absolute top-0 left-0 right-0 bottom-0 flex flex-col items-center justify-center bg-black bg-opacity-75">
          <LoaderCircle className="animate-spin"/>
          <span className="text-sm font-bold text-center w-3/4">Loading||Success</span>
        </div>
      )}
    </div>
  )
}


async function resizeImage(file: File): Promise<string> {
  const MAX_WIDTH = 250;

  return new Promise((resolve, reject) => {
    const reader = new FileReader();

    reader.onload = (event) => {
      const img = new Image();
      img.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');

        if (!ctx) {
          reject(new Error("Failed to get canvas context."));
          return;
        }

        const scale = Math.min(1, MAX_WIDTH / img.width); // Scale down if necessary
        const width = img.width * scale;
        const height = img.height * scale;

        canvas.width = width;
        canvas.height = height;

        ctx.drawImage(img, 0, 0, width, height);

        // Convert canvas to Blob and create Object URL
        canvas.toBlob(
          (blob) => {
            if (blob) {
              resolve(URL.createObjectURL(blob)); // Return Object URL
            } else {
              reject(new Error("Failed to create Blob from canvas."));
            }
          },
          file.type || 'image/jpeg', // Keep the original file type or use default
          0.8 // Quality (0.0 to 1.0)
        );
      };

      img.onerror = () => reject(new Error("Failed to load image."));
      if (event.target?.result) {
        img.src = event.target.result as string;
      } else {
        reject(new Error("FileReader did not produce a result."));
      }
    };

    reader.onerror = () => reject(new Error("Failed to read file."));
    reader.readAsDataURL(file);
  });
}

