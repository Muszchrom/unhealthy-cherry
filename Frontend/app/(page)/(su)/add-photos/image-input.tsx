"use client"

import { cn } from "@/lib/utils";
import { Upload } from "lucide-react";
import React, { useRef, useState } from "react";
import PhotosPreview from "./photos-preview";
import { Heading2 } from "@/components/headings";

interface ImageInputProps {
  photos: File[],
  setPhotos: (photos: File[]) => void
}

export default function ImageInput({photos, setPhotos}: ImageInputProps) {
  const [playAnimation, setPlayAnimation] = useState(false);
  
  const inputRef = useRef<HTMLInputElement>(null);
  const dropBox = useRef<HTMLDivElement>(null);


  const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setPhotos(Array.from(photos).concat(Array.from(e.dataTransfer.files)))
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files === null) return;
    setPhotos(Array.from(photos).concat(Array.from(e.target.files)));
  }

  return (
    <>
      <div className="w-full flex flex-col items-center">
        <Heading2 className="pb-2">Drop files here</Heading2>
        <div className="border w-full aspect-video rounded-md flex items-center justify-center max-w-md"
            onDrop={handleDrop}
            onDragOver={e => e.preventDefault()}
            onDragEnter={() => setPlayAnimation(true)}
            onDragLeave={() => setPlayAnimation(false)}
            onClick={() => inputRef.current?.click()}
            ref={dropBox}
            >
          <Upload className={cn("stroke-muted-foreground", playAnimation && "bounce-reverse")}/>
          <input className="hidden" ref={inputRef} type="file" accept="image/*" multiple={true} onChange={handleChange}/>
        </div>
      </div>
    </>
  );
}

