import {
  Sheet,
  SheetContent,
  SheetClose,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet"
import { Category, Place } from "@/interfaces/interfaces";
import { cookies } from "next/headers";
import Link from "next/link";

export async function SiteHeader() {
  const token = cookies().get("JWT")?.value;

  const categories_data = await fetch("http://gateway:8081/photos/categories", {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
  
  // if you experience an Unexpected end of JSON input at this line
  // that means you do have an invalid cookie
  const categories: Category[] = await categories_data.json();

  // !!!! Places already include categories
  const places_data = await fetch("http://gateway:8081/photos/places", {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
  const places: Place[] = await places_data.json();

  return (
    <header className="">
      <div className="container flex h-14 max-w-screen-2xl items-center justify-between">
        <h1 className="scroll-m-20 text-2xl font-bold tracking-tight">Grecja</h1>
        <Sheet>
          <SheetTrigger className="h-9 py-2 ml-2 px-0 bg-transparent hover:bg-transparent focus-visible:bg-transparent focus-visible:ring-0 focus-visible:ring-offset-0 ">
            <svg strokeWidth="1.5" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 [transform:rotateZ(180deg)]">
              <path d="M3 5H11" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"></path>
              <path d="M3 12H16" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"></path>
              <path d="M3 19H21" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"></path>
            </svg>
          </SheetTrigger>
          <SheetContent side={"left"} className="flex flex-col">
            <SheetTitle aria-describedby="">Menu zdjęć</SheetTitle>
            <div className="my-4 pb-10 pr-6 flex-[1_1_0] flex flex-col">
              <div className="flex flex-col space-y-3">
                {categories.map((category) => 
                  <SheetClose asChild key={category.id}>
                    <Link href={`/${category.categoryAsPathVariable}`}>
                      {category.category}
                    </Link>
                  </SheetClose>
                )}
              </div>
              <div className="pt-6">
                <div className="flex flex-col space-y-3 pt-4">
                  <h4 className="font-medium">Miejsca</h4>
                  {places.map((place) => 
                    <SheetClose asChild key={place.id}>
                      <Link className="text-muted-foreground" href={`/${place.category.categoryAsPathVariable}/${place.placeAsPathVariable}`}>
                        {place.place}
                      </Link>
                    </SheetClose>
                  )}
                </div>
                
              </div>

              <div className="mt-auto">
              <SheetClose asChild>
                <Link href="/logout">{"Wyloguj się"}</Link>
              </SheetClose>
              </div>
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </header>
  );
}