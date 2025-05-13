"use client";

import { 
  AlertDialog, 
  AlertDialogAction, 
  AlertDialogCancel, 
  AlertDialogContent, 
  AlertDialogDescription, 
  AlertDialogFooter, 
  AlertDialogHeader, 
  AlertDialogTitle, 
  AlertDialogTrigger } from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { TableCell, TableRow } from "@/components/ui/table";
import { Check, LoaderCircle, X } from "lucide-react";
import { useState } from "react";

export default function TableRowComponent({username, handleAccept, handleDecline}: {username: string, handleAccept: (username: string) => void, handleDecline: (username: string) => void}) {
  const [pending, setPending] = useState(false);

  const handleClick = (accepted: boolean) => {
    setPending(true);
    setTimeout(() => {
      if (accepted) {
        handleAccept(username);
      } else {
        handleDecline(username);
      }
      setPending(false);
    }, 1000);
  }

  return (
    <TableRow>
      <TableCell className="font-medium">{username}</TableCell>
      <TableCell>
        <Alert handleClick={() => handleClick(true)} user={username} disabled={pending}/>
      </TableCell>
      <TableCell>
        <Alert variant="destructive" handleClick={() => handleClick(false)} user={username} disabled={pending}/>
      </TableCell>
    </TableRow>
  )
}

interface AlertProps {
  user: string, 
  handleClick: () => void, 
  disabled: boolean
  variant?: "destructive", 
}

function Alert({variant, user, handleClick, disabled}: AlertProps) {
  return (
    <AlertDialog>
      <AlertDialogTrigger asChild>
        <Button variant={variant} disabled={disabled} className="flex h-fit w-fit py-1 px-2 mx-auto">
          {disabled 
            ? <LoaderCircle className="animate-spin"/>
            : variant === "destructive" ? <X /> : <Check />
          }
        </Button>
      </AlertDialogTrigger>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>
            Are you absolutely sure?
          </AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot yet be undone. 
            {variant === "destructive" ? ` ${user} will gain access to the photos` : ` ${user} will NOT gain access to the photos`}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction onClick={handleClick}>Continue</AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}