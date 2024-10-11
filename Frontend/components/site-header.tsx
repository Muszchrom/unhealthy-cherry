import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetPortal,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet"
import Link from "next/link";

export function SiteHeader() {
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
          <SheetContent side={"left"}>
            <SheetTitle aria-describedby="">Menu zdjęć</SheetTitle>
              <div className="my-4 pb-10 pr-6">
                <div className="flex flex-col space-y-3">
                  <Link href="/">Grecja</Link>
                  <Link href="/e">Góry 2021</Link>
                  <a href="/">Bieszczady</a>
                  <a href="/">Góry stołowe 2022</a>
                </div>
                <div className="pt-6">
                  <div className="flex flex-col space-y-3 pt-4">
                    <h4 className="font-medium">Kraje</h4>
                    <a className="text-muted-foreground" href="/">Polska</a>
                    <a className="text-muted-foreground" href="/">Włochy</a>
                    <a className="text-muted-foreground" href="/">Grecja</a>
                  </div>
                  <div className="flex flex-col space-y-3 pt-4">
                    <h4 className="font-medium">Daty</h4>
                    <a className="text-muted-foreground" href="/">Luty 2020</a>
                    <a className="text-muted-foreground" href="/">Lipiec 2020</a>
                    <a className="text-muted-foreground" href="/">Wrzesień 2021</a>
                    <a className="text-muted-foreground" href="/">Październik 2022</a>
                  </div>
                </div>
              </div>
          </SheetContent>
        </Sheet>
      </div>
    </header>
  );
}