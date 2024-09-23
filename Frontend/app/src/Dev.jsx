import { useRef, useState } from "react"

export default function Dev() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const singleFile = useRef(null);
  const multiFile = useRef(null);

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(username);
    console.log(password);
    // (async () => {
    //   const res = await fetch("http://localhost:8080/photos", {
    //     method: "POST",
    //     headers: {
    //       "Content-Type": "application/json"
    //     },
    //     body: JSON.stringify({
    //       fileName: "ddd",
    //       category: "wtr",
    //     })
    //   });
    //   if (res.status == 400) {
    //     console.log(await res.text());
    //     return
    //   }
    //   const data = await res.json();
    //   console.log(data);
    // })()

    (async () => {
      const res = await fetch("http://localhost:8080/photos")
      const data = await res.json();
      console.log(data);
    })()
  }

  const handleFuckedFetch = () => {
    (async () => {
      const res = await fetch("http://localhost:8080/photos/420");
      if (res.status == 404) {
        // console.log(res.headers.get("Content-Type"));
        console.log(await res.text());
        return
      }
      const data = await res.json();
      console.log(data);
    })()
  }

  const handleImageUpload = () => {
    console.log();
    Array.from(multiFile.current.files).forEach(e => {
      console.log(e);      
    });

    const formData = new FormData();
    formData.append("file", singleFile.current.files[0]);
    formData.append("placeId", 1);

    (async () => {
      const res = await fetch("http://localhost:8080/file/upload", {
        method: "POST",
        body: formData
      });
      console.log(await res.text());
      console.log(res.status);
      console.log(res.headers.get("Content-Type"));
    })()
  }

  const examplePhotoObject = {      
    id: 1,
    fileName: "Wed",
    fileExtension: ".cracked",
    place: {
      id: 1,
      place: "Santorini",
      placeAsPathVariable: "santorini",
      category: {
        id: 1,
        category: "Grecja",
        categoryAsPathVariable: "grecja"
      }
    },
    description: "A photo of a snake",
    isBest: true,
    country: "AH CANT SAY THAT EITHER",
    camera: "IP 12.5",
    datetime: null
  }

  const examplePOSSIBLEUpdates = {      
    // fileName: "!BASED ON FILE ITSELF",
    // fileExtension: "!BASED ON FILE ITSELF",
    place: { // (PLACE) OR (PLACE AND CATEGORY)
      id: 1,
      category: {
        id: 1,
      }
    },
    description: "A photo of a snake",
    isBest: true,
    country: "AH CANT SAY THAT EITHER",
    camera: "IP 12.5",
    datetime: 1
  }

  const weed = {      
    // fileName: "!BASED ON FILE ITSELF",
    // fileExtension: "!BASED ON FILE ITSELF",
  }

  const handlePatchTest = () => {
    const formData = new FormData();
    formData.append("photo", JSON.stringify({
      id: 1,
      fileName: "Wed",
      fileExtension: ".cracked",
      place: {
        id: 1
      },
      description: "A photo of a snake",
      isBest: true,
      country: "AH CANT SAY THAT EITHER",
      camera: "IP 12.5",
    }));

    (async () => {
      const res = await fetch("http://localhost:8080/photos/1", {
        method: "PATCH",
        body: formData
      });
    })()
  }

  return (
    <>
      <form onSubmit={handleSubmit} className="flex flex-col gap-4 bg-zinc-900 py-4 px-2 rounded-md">
        <p>Hello world</p>
        <input onChange={(e) => setUsername(e.target.value)} type="text" className="bg-zinc-800 border-2 border-zinc-600 rounded-sm p-1"/>
        <input onChange={(e) => setPassword(e.target.value)} type="password" className="bg-zinc-800 border-2 border-zinc-600 rounded-sm p-1"/>
        <button type="submit" className="bg-zinc-800">Next</button>
        <button type="button" onClick={handleFuckedFetch}>Fetch</button>
        <input ref={singleFile} type="file" accept=".jpg"></input>
        <input ref={multiFile} type="file" multiple></input>
        <button type="button" onClick={handleImageUpload}>ee</button>
        <button type="button" onClick={handlePatchTest}>Patch test</button>
      </form>
      <img src="http://localhost:8080/file/0fe6fb5d7cc48fe768697b20bdbddc506f0d6abd07bc38f3bad4131f2683fb51.jpg"/>
    </>
  )
}
// useEffect(() => {
//   (async () => {
//     const res = await fetch("http://localhost:8080/photos/1")
//     const data = await res.json()
//     console.log(data)
//   })()
// }, [])