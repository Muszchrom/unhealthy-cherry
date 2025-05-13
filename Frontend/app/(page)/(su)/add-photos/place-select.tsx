"use client"

import { Check, ChevronsUpDown } from "lucide-react"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command"
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import { useEffect, useState } from "react"
import { Category, Place } from "@/interfaces/interfaces"
import { toast } from "sonner"
import { API_URL_CLIENT } from "@/lib/urls"
import AddPlaceDialog from "./add-place"

interface PlaceSelectProps {
  bearer: string, 
  category: Category, 
  setPlace: (place: Place | undefined) => void
}

export function PlaceSelect({bearer, category, setPlace}: PlaceSelectProps) {
  const [places, setPlaces] = useState<Place[]>([]);
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState("");

  useEffect(() => {
    setValue("");
    if (category?.id === undefined) return;
    (async () => {
      const res = await fetch(API_URL_CLIENT + "/photos/places?categoryid=" + category.id, {
        method: "GET",
        headers: {
          "Authorization": bearer 
        }
      })
      if (!res) return;
      if (res.status != 200) return toast.error("An error occured while fetching data from the server");
      const data: Place[] = await res.json();
      setPlaces(data);
    })()
  }, [category]);

  useEffect(() => {
    setPlace(places.find((place) => "" + place.id === value))
  }, [value]);

  const uploadPlace = async (place: string, pathVariable: string) => {
    const res = await fetch(API_URL_CLIENT + "/photos/places", {
      method: "POST",
      headers: {
        "Authorization": bearer,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        category: category,
        place: place,
        placeAsPathVariable: pathVariable,
      })
    });
    const plac: Place = await res.json();
    if (res.status != 200) {
      toast.error("An error occured during upload");
      return false;
    }

    setPlaces([...places, {
      id: plac.id,
      place: plac.place,
      placeAsPathVariable: plac.placeAsPathVariable,
      category: plac.category
    }]);
    setValue("" + plac.id);
    return true;
  }

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          disabled={category?.id === undefined}
          className="w-[200px] justify-between"
        >
          {value
            ? places.find((place) => "" + place.id === value)?.place
            : "Select category..."}
          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[200px] p-0">
        <Command>
          <CommandInput placeholder="Search category..." />
          <CommandList>
            <CommandEmpty>
              <div className="flex flex-col gap-2 px-4">
                <span>No place found.</span>
                <AddPlaceDialog submitFunction={uploadPlace} />
              </div>
            </CommandEmpty>
            <CommandGroup>
              {places.map((place) => (
                <CommandItem
                  key={place.id}
                  value={"" + place.id}
                  onSelect={(currentValue: string) => {
                    setValue(currentValue === value ? "" : currentValue)
                    setOpen(false)
                  }}
                >
                  <Check
                    className={cn(
                      "mr-2 h-4 w-4",
                      value === "" + place.id ? "opacity-100" : "opacity-0"
                    )}
                  />
                  {place.place}
                </CommandItem>
              ))}
              {!!places.length && (
                <div className="py-6 px-4">
                  <AddPlaceDialog submitFunction={uploadPlace} />
                </div>
              )}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  )
}
