

// // // Get theme from localStorage
// function getTheme() {
//   return localStorage.getItem("theme") || "light"; // default to light
// }

// // Set theme in localStorage and apply to <html>
// function setTheme(theme) {
//   localStorage.setItem("theme", theme);
//   document.querySelector('html').classList.remove("light", "dark");
//   document.querySelector('html').classList.add(theme);
  
//   console.log("Theme set to:", theme);
// }

// // Toggle theme on button click
// function toggleTheme() {
//   let toggle = document.querySelector("#themeBtn");

//   toggle.addEventListener("click", () => {
//     let currentTheme = getTheme();
//     let newTheme = currentTheme === "dark" ? "light" : "dark";
//     setTheme(newTheme);
 
//   console.log("theme changed");
//   });
// }

// // Apply theme on page load
// document.addEventListener("DOMContentLoaded", () => {
//   setTheme(getTheme());
//   toggleTheme();
// });





// 1. Toggle dropdown visibility
function toggleExportDropdown() {
    let dropdown = document.getElementById('exportDropdown');
    dropdown.classList.toggle('hidden');
}

// Close dropdown when clicking anywhere outside of it
window.addEventListener('click', function(e) {
    let dropdown = document.getElementById('exportDropdown');
    let button = e.target.closest('button[onclick="toggleExportDropdown()"]');
    
    if (!button && !dropdown.contains(e.target)) {
        dropdown.classList.add('hidden');
    }
});

// 2. Excel / CSV Export Function
function exportData() {
    console.log("Exporting customized contact data...");
    
    let table = document.getElementById('contact-table');
    if (!table) {
        console.error("Table with ID 'contact-table' not found!");
        return;
    }

    let csvRows = [];

    // Define clean individual column headers
    let headers = ["Name", "Email", "Favorite", "Phone", "Website", "LinkedIn", "X (Twitter)", "Instagram"];
    csvRows.push(headers.map(field => `"${field}"`).join(","));

    // Loop through each row in the table body
    let rows = table.querySelectorAll('tbody tr');

    rows.forEach(row => {
        let cols = row.querySelectorAll('td');
        if (cols.length < 4) return;

        // Extract Name, Email, and Favorite from Column 0
        let nameSpan = cols[0].querySelector('.font-semibold');
        let name = nameSpan ? nameSpan.innerText.trim() : "";
        
        let emailP = cols[0].querySelector('p');
        let email = emailP ? emailP.innerText.trim() : "";
        
        let favoriteSpan = cols[0].querySelector('[title="Favorite"]');
        let favorite = favoriteSpan ? "Yes" : "No";

        // Extract Phone from Column 1
        let phoneSpan = cols[1].querySelector('span span');
        let phone = phoneSpan ? phoneSpan.innerText.trim() : "Not Provided";

        // Extract direct URL links from Column 2
        let websiteLink = cols[2].querySelector('a[title="Website"]');
        let website = websiteLink ? websiteLink.href : "";

        let linkedinLink = cols[2].querySelector('a[title="LinkedIn"]');
        let linkedin = linkedinLink ? linkedinLink.href : "";

        let xLink = cols[2].querySelector('a[title="X"]');
        let x = xLink ? xLink.href : "";

        let instaLink = cols[2].querySelector('a[title="Instagram"]');
        let insta = instaLink ? instaLink.href : "";

        // Package row fields securely for spreadsheet generation
        let rowData = [
            `"${name.replace(/"/g, '""')}"`,
            `"${email.replace(/"/g, '""')}"`,
            `"${favorite}"`,
            `"${phone.replace(/"/g, '""')}"`,
            `"${website}"`,
            `"${linkedin}"`,
            `"${x}"`,
            `"${insta}"`
        ];

        csvRows.push(rowData.join(","));
    });

    // Generate and trigger file download
    let csvString = csvRows.join("\n");
    let blob = new Blob([csvString], { type: 'text/csv;charset=utf-8;' });
    
    let url = URL.createObjectURL(blob);
    let a = document.createElement('a');
    a.setAttribute('href', url);
    a.setAttribute('download', 'contacts_detailed_export.csv');
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    
    console.log("Detailed export downloaded successfully!");
}

// 3. PDF Export Function
function exportPdf() {
    console.log("Generating PDF with clickable links...");
    
    let table = document.getElementById('contact-table');
    if (!table) {
        console.error("Table with ID 'contact-table' not found!");
        return;
    }

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // 1. Title for your PDF report
    doc.setFontSize(16);
    doc.text("All Contact List", 14, 15);
    doc.setFontSize(10);
    doc.setTextColor(100);
    doc.text("Smart Contact Manager (SCM) Export", 14, 22);

    // 2. Define headers and data array
    let headers = [["Name", "Email", "Favorite", "Phone", "Website", "LinkedIn", "X", "Instagram"]];
    let data = [];

    let rows = table.querySelectorAll('tbody tr');
    rows.forEach(row => {
        let cols = row.querySelectorAll('td');
        if (cols.length < 4) return;

        let name = cols[0].querySelector('.font-semibold')?.innerText.trim() || "";
        let email = cols[0].querySelector('p')?.innerText.trim() || "";
        let favorite = cols[0].querySelector('[title="Favorite"]') ? "★ Yes" : "No";
        let phone = cols[1].querySelector('span span')?.innerText.trim() || "Not Provided";
        
        // For links, store the raw URL or an empty string if not provided
        let website = cols[2].querySelector('a[title="Website"]')?.href || "";
        let linkedin = cols[2].querySelector('a[title="LinkedIn"]')?.href || "";
        let x = cols[2].querySelector('a[title="X"]')?.href || "";
        let insta = cols[2].querySelector('a[title="Instagram"]')?.href || "";

        // Display a clean text label like "Link" if the URL exists, or empty if blank
        data.push([
            name, 
            email, 
            favorite, 
            phone, 
            website ? "Open Link" : "", 
            linkedin ? "Open Link" : "", 
            x ? "Open Link" : "", 
            insta ? "Open Link" : ""
        ]);
    });

    // We can map raw URLs back during the table draw hook
    let rawUrls = [];
    rows.forEach(row => {
        let cols = row.querySelectorAll('td');
        if (cols.length < 4) return;
        rawUrls.push({
            website: cols[2].querySelector('a[title="Website"]')?.href || "",
            linkedin: cols[2].querySelector('a[title="LinkedIn"]')?.href || "",
            x: cols[2].querySelector('a[title="X"]')?.href || "",
            insta: cols[2].querySelector('a[title="Instagram"]')?.href || ""
        });
    });

    // 3. Generate the table
    doc.autoTable({
        head: headers,
        body: data,
        startY: 28,
        theme: 'grid',
        styles: { fontSize: 8, cellPadding: 3 },
        headStyles: { fillColor: [31, 41, 55] },
        columnStyles: {
            4: { textColor: [37, 99, 235] }, // Make link columns blue text
            5: { textColor: [37, 99, 235] },
            6: { textColor: [37, 99, 235] },
            7: { textColor: [37, 99, 235] }
        },
        // 4. Hook to inject clickable links into specific cells
        didDrawCell: function (data) {
            // Check if we are in the body section and inside columns 4, 5, 6, or 7
            if (data.section === 'body' && data.column.index >= 4 && data.column.index <= 7) {
                let rowIndex = data.row.index;
                let colIndex = data.column.index;
                
                let urlMap = ['website', 'linkedin', 'x', 'insta'];
                let key = urlMap[colIndex - 4];
                let actualUrl = rawUrls[rowIndex] ? rawUrls[rowIndex][key] : "";

                // If a valid URL exists for this cell, add a clickable link overlay over the cell box
                if (actualUrl) {
                    doc.link(data.cell.x, data.cell.y, data.cell.width, data.cell.height, { url: actualUrl });
                }
            }
        }
    });

    // 5. Save the PDF
    doc.save('contacts_export.pdf');
    console.log("PDF exported successfully with clickable links!");
}