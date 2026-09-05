console.log("contact details js called");
const url = "http://localhost:8080"




// Lightweight Custom Modal Class (Zero Flowbite JS dependency needed)
class Modal {
    constructor(contactModal, options = {}, instanceOptions = {}) {
        this.contactModal = contactModal;
        this.options = Object.assign({
            placement: 'bottom-right',
            backdrop: 'dynamic',
            backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
            closable: true,
            onHide: () => {},
            onShow: () => {},
            onToggle: () => {}
        }, options);
        
        this.instanceOptions = Object.assign({
            id: 'view_contact_modal',
            override: true
        }, instanceOptions);

        this.isHidden = true;
        this._backdropEl = null;
        
        this._init();
    }

    _init() {
        if (!this.contactModal) return;
        // Ensure initial state is hidden
        this.contactModal.classList.add('hidden');
        this.contactModal.classList.remove('flex');
        this.contactModal.style.display = 'none';
    }

    show() {
        if (!this.contactModal) return;

        // Create backdrop if dynamic
        if (this.options.backdrop === 'dynamic' && !this._backdropEl) {
            this._backdropEl = document.createElement('div');
            this._backdropEl.className = this.options.backdropClasses;
            document.body.appendChild(this._backdropEl);

            if (this.options.closable) {
                this._backdropEl.addEventListener('click', () => this.hide());
            }
        }

        // Show modal via Tailwind classes
        this.contactModal.classList.remove('hidden');
        this.contactModal.classList.add('flex');
        this.contactModal.style.display = 'flex';
        this.isHidden = false;

        if (typeof this.options.onShow === 'function') {
            this.options.onShow(this);
        }
    }

    hide() {
        if (!this.contactModal) return;

        // Hide modal
        this.contactModal.classList.remove('flex');
        this.contactModal.classList.add('hidden');
        this.contactModal.style.display = 'none';
        this.isHidden = true;

        // Remove backdrop
        if (this._backdropEl) {
            this._backdropEl.remove();
            this._backdropEl = null;
        }

        if (typeof this.options.onHide === 'function') {
            this.options.onHide(this);
        }
    }

    toggle() {
        if (this.isHidden) {
            this.show();
        } else {
            this.hide();
        }
        if (typeof this.options.onToggle === 'function') {
            this.options.onToggle(this);
        }
    }
}

// ==========================================
// Usage with your exact configuration:
// ==========================================

const contactModal = document.getElementById('view_contact_modal');

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

const instanceOptions = {
    id: 'view_contact_modal',
    override: true
};

// Removed the external ES6 import entirely!
const modal = new Modal(contactModal, options, instanceOptions);


function openContactModal(){
   modal.show()
}

function closeContactModal(){
  modal.hide()
}


async function loadContactModalData(id){
  console.log(id);

  try {
    const data = await fetch (`${url}/api/contact/${id}`);
    const result = await data.json();
    console.log(result);

    document.getElementById('contactname').innerHTML = result.name
    document.getElementById('contactemail').innerHTML = result.email
    document.getElementById('contactphone').innerHTML = result.phone

   document.getElementById('contactimg').src = (result.profileLink && result.profileLink.trim() !== "") 
    ? result.profileLink 
    : "https://static.vecteezy.com/system/resources/previews/021/548/095/original/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg";

    // document.getElementById('favoritecheck').innerHTML = result.favorite ? "✨" : ""

// 1. Text Fields with Fallbacks
document.getElementById('contactaddress').innerText = (result.address && result.address.trim() !== "") 
    ? result.address 
    : 'No address provided';

document.getElementById('contactdescription').innerText = (result.description && result.description.trim() !== "") 
    ? result.description 
    : 'No description provided';


// 2. Social Links with Safety Checks
// (This hides the link element or disables it if the link is empty)
function setSocialLink(elementId, url) {
    const el = document.getElementById(elementId);
    if (!el) return;

    if (url && url.trim() !== "") {
        el.href = url;
        el.style.display = "inline-flex"; // or "block" depending on your layout
    } else {
        el.href = "#";
        el.style.display = "none"; // Hide the icon if no link exists
    }
}


let favoritecontact = document.getElementById('contactfavorite');

if(result.favorite){
    favoritecontact.innerHTML = '💗';
}




setSocialLink('contactinsta', result.instaLink);
setSocialLink('contactwebsite', result.websiteLink);
setSocialLink('contactlinkedin', result.linkdinLink);
setSocialLink('contactx', result.xLink);

     openContactModal()
    return result;

  } catch (error) {
    console.log(error);
    
  }
  
}
// Now you can use modal.show() and modal.hide() anywhere safely without Flowbite JS.




// ==========Delete contact =============

async function deleteContact(id){
   Swal.fire({
  title: "Do you want to Delete the contact?",
  icon:"warning", 
  showCancelButton: true,
  confirmButtonText: "Delete",
  denyButtonText: `Cancle`
}).then((result) => {
  /* Read more about isConfirmed, isDenied below */
  if (result.isConfirmed) {
    window.location.replace(`${url}/user/contact/delete/${id}`)
  }
  else if (result.isDenied) {
    
  };
});
}









