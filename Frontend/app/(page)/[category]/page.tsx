export default function Category({params}: {params: {category: string}}) {
  return (
    <main className="flex-1">
      <div className="border-b">
        <div className="container">
          <h1>Category: {params.category}</h1>
        </div>
      </div>
    </main>
  )
}