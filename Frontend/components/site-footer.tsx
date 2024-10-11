export function SiteFooter() {
  return (
    <footer className="py-6 md:px-8 md:py-0">
      <div className="container flex flex-col items-center justify-between gap-4 md:h-24 md:flex-row">
        <p className="text-balance text-center text-sm leading-loose text-muted-foreground md:text-left">
          Stolen by Muszchrom.
          From <a href="https://twitter.com/shadcn" target="_blank" rel="no_referrer" className="font-medium underline underline-offset-4">shadcn</a>.
          Code available on <a href="https://github.com/Muszchrom" target="_blank" rel="no_referrer" className="font-medium underline underline-offset-4">GitHub</a>
        </p>
      </div>
    </footer>
  )
}