export default function Trip({params}: {params: {trip: string}}) {
  return (
    <main className="flex-1">
      <div className="border-b">
        <div className="container">
          <h1>Trip id: {params.trip}</h1>
        </div>
      </div>
    </main>
  )
}