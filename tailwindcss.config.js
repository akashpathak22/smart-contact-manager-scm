/** @type {import('tailwindcss').Config} */
module.exports = {
  
content: [
    "./src/main/resources/templates/**/*.html", // Ensure your parent.html is here
    "./src/main/resources/static/**/*.js"
  ],
  theme: {
    extend: {
      // You can add custom colors, fonts, spacing, etc. here
      colors: {
        brandBlue: "#1e40af",
        brandGray: "#374151",
      },
    },
  },
  plugins: [],
  darkMode : 'class',
}
