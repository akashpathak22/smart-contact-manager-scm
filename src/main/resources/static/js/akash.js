console.log("akash is working to preview uploaded image!");

document.addEventListener("DOMContentLoaded", () => {
    const fileInput = document.getElementById('uploaded_image_preview_input');
    
    // Only add the listener if the element actually exists on this page
    if (fileInput) {
        fileInput.addEventListener('change', (event) => {
            let image = event.target.files[0];

            if (!image) {
                return; // Prevent crash if user cancels file selection
            }

            let reader = new FileReader();

            reader.onload = function () {
                const previewImg = document.getElementById('uploaded_image_preview');
                if (previewImg) {
                    previewImg.setAttribute('src', reader.result);
                }
            }

            reader.readAsDataURL(image);
        });
    }
});



document.addEventListener("DOMContentLoaded", () => {
    const fileInput = document.getElementById('user_profile_edit_preview');
    
    // Only add the listener if the element actually exists on this page
    if (fileInput) {
        fileInput.addEventListener('change', (event) => {
            let image = event.target.files[0];

            if (!image) {
                return; // Prevent crash if user cancels file selection
            }

            let reader = new FileReader();

            reader.onload = function () {
                const previewImg = document.getElementById('uploaded_image_preview');
                if (previewImg) {
                    previewImg.setAttribute('src', reader.result);
                }
            }

            reader.readAsDataURL(image);
        });
    }
});



