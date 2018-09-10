using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

using StampieAppServer.Data.Entities;
using StampieAppServer.Models;

namespace StampieAppServer.Areas.WebApi.Controllers
{
    public class PhotoController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("photo/getAllPhotos")]
        public IEnumerable<Photo> GetAllPhotos()
        {
            return db.Photos.Include(c => c.Creator);
        }

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("photo/getPhotoById")]
        public Photo GetPhotoById(Guid id)
        {
            return db.Photos.Include(c => c.Creator).FirstOrDefault(p => p.Id == id);
        }

        [HttpPost]
        [Authorize]
        [System.Web.Mvc.Route("photo/addPhoto")]
        public Photo AddPhoto([FromBody] Photo photo)
        {
            photo.Creator = db.AppUsers.FirstOrDefault(u => u.Id == photo.Creator.Id);

            photo = db.Photos.Add(photo);
            db.SaveChanges();

            return photo;
        }
    }
}
