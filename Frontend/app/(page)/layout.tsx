import { SiteFooter } from "@/components/site-footer"
import { SiteHeader } from "@/components/site-header"

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <>
      <SiteHeader />
      <main className="container w-full px-4 flex flex-col flex-1 gap-8 row-start-2 items-center sm:items-start ">
        {children}
      </main>
      <SiteFooter />
    </>
  )
}