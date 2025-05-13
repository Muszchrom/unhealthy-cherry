import React from "react";
import { Heading1 } from "./headings";

export const Jumbo = ({children, description}: {children?: React.ReactNode, description?: string}) => (
  <div className="py-8 w-full">
    <Heading1>
      {children}
    </Heading1>
    {!!description && (
      <p className="text-muted-foreground">
        {description}
      </p>
    )}
  </div>
);

