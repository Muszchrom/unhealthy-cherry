export default function Place({params}: {params: {category: string, place: string}}) {
  return (
    <main className="flex-1">
      <div className="border-b">
        <div className="container">
          <h1>Category: {params.category}</h1>
          <h1>Place: {params.place}</h1>
        </div>
      </div>
    </main>
  )
}