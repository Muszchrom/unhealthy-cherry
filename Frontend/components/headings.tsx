import { cn } from "@/lib/utils";

interface HeadingProps {
  children: React.ReactNode,
  className?: string,
  variant?: "primary" | "secondary"
}

function Heading1({ children, className }: HeadingProps) {
  return (
    <h1 className={cn("text-2xl font-extrabold md:text-4xl", className)}>
      { children }
    </h1>
  );
}

function Heading2({ children, className, variant }: HeadingProps) {
  return (
    <h2 className={cn("text-xl", className, variant === "secondary" ? "text-foreground" : "text-primary")}>
      { children }
    </h2>
  );
}

function Heading3({ children, className }: HeadingProps) {
  return (
    <h3 className={cn("text-primary text-md", className)}>
      { children }
    </h3>
  );
}

export { Heading1, Heading2, Heading3 }