import { authOptions } from "@/app/api/auth/[...nextauth]/route"
import ProtectedImage from "@/components/protected-image";
import { Photo } from "@/interfaces/interfaces";
import { API_URL_CLIENT, API_URL_SERVER } from "@/lib/urls";
import { getServerSession } from "next-auth"

export default async function Category({params}: {params: {category: string}}) {
  const session = await getServerSession(authOptions);
  if (!session) return;
  console.log(API_URL_SERVER + "/photos/photos/" + params.category)
  const res = await fetch(API_URL_SERVER + "/photos/photos/" + params.category, {
    method: "GET",
    headers: {
      "Authorization": "Bearer " + session?.user.APIToken
    }
  });
  console.log(res)
  // console.log(await res.text())
  const images: Photo[] = await res.json();

  return (
    <>
      <h1>Category: {params.category}</h1>
      <div className="grid w-full grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-2 ">
        {images.map((image) => {
          return (
            <div className="w-full aspect-square relative" key={image.id}>
              <ProtectedImage url={API_URL_CLIENT + "/photos/photo/" + image.fileName + "." + image.fileExtension} bearer={"Bearer " + session?.user.APIToken} />
            </div>
            // <img src={API_URL_CLIENT + "/photos/photo/" + image.fileName + "." + image.fileExtension} />
          )
        })}
      </div>
    </>
  )
}