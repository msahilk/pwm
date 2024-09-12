/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/renderer/index.html",
    "./src/renderer/src/components/*.{js,ts,jsx,tsx}",
    "./src/renderer/src/*.{js,jsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}

