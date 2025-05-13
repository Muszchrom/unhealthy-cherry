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
import { Category } from "@/interfaces/interfaces"
import { toast } from "sonner"
import { API_URL_CLIENT } from "@/lib/urls"
import AddCategoryDialog from "./add-category"

export function CategorySelect({bearer, setCategory}: {bearer: string, setCategory: (category: Category | undefined) => void}) {
  const [categories, setCategories] = useState<Category[]>([]);
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState("");

  useEffect(() => {
    (async () => {
      const res = await fetch(API_URL_CLIENT + "/photos/categories", {
        method: "GET",
        headers: {
          "Authorization": bearer
        }
      })
      if (!res) return;
      if (res.status != 200) return toast.error("An error occured while fetching data from the server");
      const data: Category[] = await res.json();
      setCategories(data);
    })()
  }, []);

  useEffect(() => {
    setCategory(categories.find((category) => "" + category.id === value))
  }, [value]);

  const uploadCategory = async (category: string, pathVariable: string) => {
    const res = await fetch(API_URL_CLIENT + "/photos/categories", {
      method: "POST",
      headers: {
        "Authorization": bearer,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        category: category,
        categoryAsPathVariable: pathVariable
      })
    });
    const cat: Category = await res.json();
    if (res.status != 200) {
      toast.error("An error occured during upload");
      return false; 
    } 
    
    setCategories([...categories, {
      id: cat.id, 
      category: cat.category, 
      categoryAsPathVariable: cat.categoryAsPathVariable
    }]);
    setValue("" + cat.id);
    return true;
  }

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-[200px] justify-between"
        >
          {value
            ? categories.find((category) => "" + category.id === value)?.category
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
                <span>No category found.</span>
                <AddCategoryDialog submitFunction={uploadCategory}/>
              </div>
            </CommandEmpty>
            <CommandGroup>
              {categories.map((category) => {
                return (
                  <CommandItem
                    key={category.id}
                    value={"" + category.id}
                    onSelect={(currentValue: string) => {
                      setValue(currentValue === value ? "" : currentValue)
                      setOpen(false)
                    }}
                  >
                    <Check
                      className={cn(
                        "mr-2 h-4 w-4",
                        value === "" + category.id ? "opacity-100" : "opacity-0"
                      )}
                    />
                    {category.category}
                  </CommandItem>
                )
              })}
              {!!categories.length && (
                <div className="py-6 px-4">
                  <AddCategoryDialog submitFunction={uploadCategory}/>
                </div>
              )}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  )
}