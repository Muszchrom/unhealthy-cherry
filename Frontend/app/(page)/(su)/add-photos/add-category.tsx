import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Label } from "@/components/ui/label"
import { Input } from "@/components/ui/input"
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { LoaderCircle } from "lucide-react";

interface AddCategoryDialogProps {
  submitFunction: (category: string, pathVariable: string) => Promise<boolean>,
}
export default function AddCategoryDialog({submitFunction}: AddCategoryDialogProps) {
  const [category, setCategory] = useState("");
  const [pathVariable, setPathVariable] = useState("");
  const [loading, setLoading] = useState(false);

  const [open, setOpen] = useState(false);

  const handleClick = async () => {
    setLoading(true);
    console.log(category, pathVariable);
    const val = await submitFunction(category, pathVariable)
    if (val) setOpen(false)
    setLoading(false);
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        {/* Button */}
        <Button variant="default" className="w-full" onClick={() => setOpen(true)}>Add category</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Add category</DialogTitle>
          <DialogDescription>Add a new category. Click save when you're done.</DialogDescription>
        </DialogHeader>

        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="name" className="text-right">
              Category
            </Label>
            <Input id="name" value={category} onChange={(e) => setCategory(e.target.value)} placeholder="Trasa bobrówka" className="col-span-3" />
          </div>
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="username" className="text-right">
              Path variable
            </Label>
            <Input id="username" value={pathVariable} onChange={(e) => setPathVariable(e.target.value)} placeholder="trasa-bobrowka" className="col-span-3" />
          </div>
        </div>

        <DialogFooter>
          {/* Button confirm */}
          <Button disabled={loading} type="submit" onClick={handleClick}>
            {loading ? (
              <LoaderCircle className="animate-spin"/>
            ) : (
              <span>Save changes</span>
            )}
            </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
