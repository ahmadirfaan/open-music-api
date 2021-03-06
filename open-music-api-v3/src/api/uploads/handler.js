const errorHandler = require('../../utils/ErrorHandler');

class UploadsHandler {
  constructor(service, validator) {
    this._service = service;
    this._validator = validator;

    this.postUploadImageHandler = this.postUploadImageHandler.bind(this);
  }

  async postUploadImageHandler(request, h) {
    try {
      const { data } = request.payload;
      this._validator.validateImageHeaders(data.hapi.headers);
      const filename = await this._service.writeFile(data, data.hapi);
      const response = h.response({
        status: 'success',
        data: {
          fileLocation: `http://${process.env.HOST}:${process.env.PORT}/upload/pictures/${filename}`,
        },
      });
      response.code(201);
      return response;
    } catch (error) {
      return errorHandler(error, h);
    }
  }
}

module.exports = UploadsHandler;
