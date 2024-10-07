/** Copyright © 2018, Okta, Inc.
 *
 *  Licensed under the MIT license, the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     https://opensource.org/licenses/MIT
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.service.scim.controllers;

import com.service.scim.models.User;
import com.service.scim.services.IUsersService;
import com.service.scim.utils.ListResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *  URL route (root)/scim/v2/Users
 */
@Controller
@RequestMapping("/scim/v2/Users")
public class UsersController {

    private final IUsersService usersService;


    @Autowired
    public UsersController(IUsersService usersService) {
      this.usersService = usersService;
    }

    /**
     * Support pagination and filtering by username
     * @param params Payload from HTTP request
     * @return JSON {@link Map} {@link ListResponse}
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map usersGet(@RequestParam Map<String, String> params) {

        return usersService.usersGet(params);
    }

    /**
     * Creates new {@link User} with given attributes
     * @param params JSON {@link Map} of {@link User} attributes
     * @param response HTTP response
     * @return JSON {@link Map} of {@link User}
     */
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Map usersPost(@RequestBody Map<String, Object> params, HttpServletResponse response){

        return usersService.usersPost(params, response);
    }
}
