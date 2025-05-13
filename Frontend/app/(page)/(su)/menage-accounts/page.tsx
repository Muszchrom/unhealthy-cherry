"use client";

import { Table, TableBody, TableCaption, TableHead, TableRow, TableHeader } from "@/components/ui/table";
import { useState } from "react";
import { toast } from "sonner";
import TableRowComponent from "./table-row";
import { Jumbo } from "@/components/jumbo";

export default function ManagePage() {

  const [usernames, setUsernames] = useState(["Example1", "Example2", "Example3", "Example4", "Example5", "Example6"]);

  const handleAccept = (username: string) => {
    console.log(username, "accepted");
    setUsernames(usernames.filter((v) => v !== username));
    toast.info(`Access to the website for ${username} gained`);
  }
  
  const handleDecline = (username: string) => {
    console.log(username, "denied");
    setUsernames(usernames.filter((v) => v !== username));
    toast.info(`Access to the website for ${username} denied`);
  }

  return (
    <>
      <Jumbo description="Accept or deny user reqeust for accessing the page">
        Menage accounts
      </Jumbo>
      <Table>
        <TableCaption>A list of user requesting access.</TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead className="w-full">Username</TableHead>
            <TableHead className="text-center">Accept</TableHead>
            <TableHead className="text-center">Decline</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {usernames.map((username) => (
            <TableRowComponent key={username} username={username} handleAccept={handleAccept} handleDecline={handleDecline}/>
          ))}
        </TableBody>
      </Table>
    </>
  );
}



